"use client";

import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { useEffect, useRef, useState } from "react";
import { useAuthStore } from "@/lib/zustand";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useForm, useWatch } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Label } from "@/components/ui/label";
import { Clock, MapPin, Phone, Sparkles, Upload } from "lucide-react";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { OrderSchema, OrderType } from "@/schemaValidations/order.schema";
import {
  useShopCategories,
  useShops,
  useShopServices,
} from "@/queries/useShop";
import { useOrderMutation } from "@/queries/useOrder";
import { useAccountMe } from "@/queries/useAccount";
import { toast } from "react-toastify";
import {
  compressImageToBase64,
  formatCurrency,
  handleErrorApi,
} from "@/lib/utils";
import { useRouter } from "next/navigation";

const matchesPreference = (candidate: string, preference: string) => {
  const c = candidate.trim().toLowerCase();
  const p = preference.trim().toLowerCase();
  if (!p) return false;
  return c.includes(p) || p.includes(c);
};

export default function Order() {
  const {
    openOrder,
    setOpenOrder,
    isAuth,
    preferredCategory,
    preferredService,
    draftPhone,
    clearBookingPreset,
  } = useAuthStore();
  const [shopId, setShopId] = useState<number>(0);
  const [categoryId, setCategoryId] = useState<number>(0);
  const avatarInputRef = useRef<HTMLInputElement>(null);
  const [previewAvatar, setPreviewAvatar] = useState("");

  const router = useRouter();

  const { data: shopsData, isLoading: isLoadingShops } = useShops(
    isAuth && openOrder
  );
  const allShops = shopsData?.payload.data ?? [];
  const effectiveShopId = shopId > 0 ? shopId : (allShops[0]?.id ?? 0);

  const { data: categoriesData, isFetching: isFetchingCategories } =
    useShopCategories(effectiveShopId);
  const categories = categoriesData?.payload.data ?? [];

  const hasValidSelectedCategory =
    categoryId > 0 && categories.some((c) => c.id === categoryId);
  const matchedCategory = hasValidSelectedCategory
    ? categories.find((c) => c.id === categoryId)
    : !isFetchingCategories && categories.length > 0
      ? (preferredCategory &&
          categories.find((c) => matchesPreference(c.name, preferredCategory))) ||
        categories[0]
      : undefined;
  const effectiveCategoryId = matchedCategory?.id ?? 0;

  const { data: servicesData, isFetching: isFetchingServices } =
    useShopServices(effectiveShopId, effectiveCategoryId);
  const services = servicesData?.payload.data ?? [];

  const { data: meData } = useAccountMe(isAuth && openOrder);
  const account = meData?.payload?.data?.account;
  const isNonCustomer = Boolean(account?.role && account.role !== "Customer");

  const orderMutation = useOrderMutation();

  const form = useForm<OrderType>({
    resolver: zodResolver(OrderSchema),
    defaultValues: {
      laundryShop: { id: 0 },
      serviceCategory: { id: 0 },
      service: { id: 0 },
      orderVolume: 1,
      phone: "",
      address: "",
      instructions: "",
      imgProduct: "",
    },
  });

  const watchedVolume = useWatch({
    control: form.control,
    name: "orderVolume",
  });
  const watchedServiceId = useWatch({
    control: form.control,
    name: "service.id",
  });

  const currentSvc =
    watchedServiceId > 0
      ? services.find((s) => s.id === watchedServiceId)
      : undefined;
  const effectiveService =
    currentSvc ||
    (!isFetchingServices && services.length > 0
      ? (preferredService &&
          services.find((s) => matchesPreference(s.name, preferredService))) ||
        services[0]
      : undefined);
  const selectedPrice = effectiveService?.price ?? 0;

  // Đồng bộ giá trị mặc định vào react-hook-form mà không gọi setState đồng bộ trong effect
  useEffect(() => {
    if (!openOrder || effectiveShopId <= 0) return;
    if (form.getValues("laundryShop.id") !== effectiveShopId) {
      form.setValue("laundryShop.id", effectiveShopId);
    }
  }, [openOrder, effectiveShopId, form]);

  useEffect(() => {
    if (!openOrder || effectiveCategoryId <= 0) return;
    if (form.getValues("serviceCategory.id") !== effectiveCategoryId) {
      form.setValue("serviceCategory.id", effectiveCategoryId);
    }
  }, [openOrder, effectiveCategoryId, form]);

  useEffect(() => {
    if (!openOrder || !effectiveService) return;
    if (form.getValues("service.id") !== effectiveService.id) {
      form.setValue("service.id", effectiveService.id);
    }
  }, [openOrder, effectiveService, form]);

  // Điền sẵn số điện thoại & địa chỉ từ form nhanh hoặc hồ sơ tài khoản
  useEffect(() => {
    if (!openOrder) return;
    const phonePreset = draftPhone || account?.phone || "";
    if (phonePreset && !form.getValues("phone")) {
      form.setValue("phone", phonePreset);
    }
    if (account?.address && !form.getValues("address")) {
      form.setValue("address", account.address);
    }
  }, [openOrder, draftPhone, account?.phone, account?.address, form]);

  const selectedServiceObj = effectiveService;
  const volume = watchedVolume || 0;
  const totalAmount = Number.isFinite(selectedPrice * volume)
    ? Math.round(selectedPrice * volume * 100) / 100
    : 0;

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    try {
      const base64 = await compressImageToBase64(file);
      setPreviewAvatar(base64);
      form.setValue("imgProduct", base64);
    } catch (error) {
      toast.error(
        error instanceof Error ? error.message : "Không thể xử lý hình ảnh"
      );
    }
  };

  const handleRemoveImage = () => {
    setPreviewAvatar("");
    form.setValue("imgProduct", "");
    if (avatarInputRef.current) {
      avatarInputRef.current.value = "";
    }
  };

  const reset = () => {
    form.reset({
      laundryShop: { id: 0 },
      serviceCategory: { id: 0 },
      service: { id: 0 },
      orderVolume: 1,
      phone: "",
      address: "",
      instructions: "",
      imgProduct: "",
    });
    setShopId(0);
    setCategoryId(0);
    setPreviewAvatar("");
  };

  const handleClose = () => {
    clearBookingPreset();
    reset();
    setOpenOrder(false);
  };

  useEffect(() => {
    if (!openOrder && avatarInputRef.current) {
      avatarInputRef.current.value = "";
    }
  }, [openOrder]);

  const onSubmit = async (values: OrderType) => {
    if (orderMutation.isPending || isNonCustomer) return;
    try {
      const result = await orderMutation.mutateAsync(values);
      handleClose();
      toast.success(result.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      router.push(`/thanh-toan?orderId=${result.payload.data.id}`);
    } catch (error) {
      handleErrorApi({ error, setError: form.setError });
    }
  };

  return (
    <Dialog
      open={openOrder}
      onOpenChange={(open: boolean) => {
        if (!open) {
          handleClose();
          return;
        }
        setOpenOrder(true);
      }}
    >
      <DialogContent
        className="border-none w-[calc(100vw-1.5rem)] sm:w-full max-w-lg sm:max-w-xl md:max-w-2xl p-0 flex flex-col max-h-[min(90dvh,760px)] overflow-hidden rounded-2xl sm:rounded-[24px]"
        aria-describedby={undefined}
        onInteractOutside={(e) => {
          if (orderMutation.isPending) e.preventDefault();
        }}
      >
        <div className="shrink-0 bg-black px-4 py-3.5 pr-14 sm:px-6 sm:py-4 text-white">
          <div className="inline-flex items-center gap-1.5 rounded-full bg-main px-2.5 py-0.5 text-[11px] sm:text-xs font-bold text-black mb-1.5">
            <Sparkles className="h-3.5 w-3.5" />
            HERAMO • ĐẶT DỊCH VỤ 4.0
          </div>
          <DialogTitle className="text-lg sm:text-2xl font-bold text-white leading-snug">
            Đặt lịch giặt ủi & vệ sinh
          </DialogTitle>
          <p className="mt-0.5 text-xs sm:text-sm text-gray-300">
            Chọn cửa hàng, dịch vụ và địa chỉ giao nhận — thanh toán nhanh bằng
            VietQR.
          </p>
        </div>

        <div className="flex-1 min-h-0 overflow-y-auto px-4 py-4 sm:px-6 sm:py-5">
          <Form {...form}>
            <form
              className="space-y-3.5"
              noValidate
              onSubmit={form.handleSubmit(onSubmit)}
              onReset={reset}
            >
              <FormField
                control={form.control}
                name="laundryShop.id"
                render={({ field }) => (
                  <FormItem>
                    <Label className="font-semibold">Chọn cửa hàng</Label>
                    <Select
                      value={
                        field.value || effectiveShopId
                          ? String(field.value || effectiveShopId)
                          : ""
                      }
                      onValueChange={(value) => {
                        const nextShopId = parseInt(value, 10);
                        field.onChange(nextShopId);
                        setShopId(nextShopId);
                        setCategoryId(0);
                        form.setValue("serviceCategory.id", 0);
                        form.setValue("service.id", 0);
                      }}
                    >
                      <FormControl>
                        <SelectTrigger className="w-full">
                          <SelectValue
                            placeholder={
                              isLoadingShops
                                ? "Đang tải cửa hàng..."
                                : "Chọn cửa hàng"
                            }
                          />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {allShops.map((shop) => (
                          <SelectItem
                            key={shop.id}
                            value={shop.id.toString()}
                            className="cursor-pointer"
                          >
                            {shop.name} - {shop.address}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3.5">
                <FormField
                  control={form.control}
                  name="serviceCategory.id"
                  render={({ field }) => (
                    <FormItem>
                      <Label className="font-semibold">Loại hình dịch vụ</Label>
                      <Select
                        key={`category-${effectiveShopId}`}
                        disabled={!effectiveShopId || isFetchingCategories}
                        value={
                          field.value || effectiveCategoryId
                            ? String(field.value || effectiveCategoryId)
                            : ""
                        }
                        onValueChange={(value) => {
                          const nextCategoryId = parseInt(value, 10);
                          field.onChange(nextCategoryId);
                          setCategoryId(nextCategoryId);
                          form.setValue("service.id", 0);
                        }}
                      >
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue
                              placeholder={
                                !effectiveShopId
                                  ? "Chọn cửa hàng trước"
                                  : isFetchingCategories
                                    ? "Đang tải..."
                                    : "Chọn loại dịch vụ"
                              }
                            />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {categories.length === 0 &&
                            effectiveShopId > 0 &&
                            !isFetchingCategories && (
                              <SelectItem value="__empty" disabled>
                                Cửa hàng chưa có loại dịch vụ
                              </SelectItem>
                            )}
                          {categories.map((item) => (
                            <SelectItem
                              key={item.id}
                              value={item.id.toString()}
                              className="cursor-pointer"
                            >
                              {item.name}
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="service.id"
                  render={({ field }) => (
                    <FormItem>
                      <Label className="font-semibold">Dịch vụ</Label>
                      <Select
                        key={`service-${effectiveShopId}-${effectiveCategoryId}`}
                        disabled={!effectiveCategoryId || isFetchingServices}
                        value={
                          field.value || effectiveService?.id
                            ? String(field.value || effectiveService?.id)
                            : ""
                        }
                        onValueChange={(value) => {
                          const nextServiceId = parseInt(value, 10);
                          field.onChange(nextServiceId);
                        }}
                      >
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue
                              placeholder={
                                !effectiveCategoryId
                                  ? "Chọn loại dịch vụ trước"
                                  : isFetchingServices
                                    ? "Đang tải..."
                                    : "Chọn dịch vụ"
                              }
                            />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {services.length === 0 &&
                            effectiveCategoryId > 0 &&
                            !isFetchingServices && (
                              <SelectItem value="__empty" disabled>
                                Chưa có dịch vụ trong mục này
                              </SelectItem>
                            )}
                          {services.map((item) => (
                            <SelectItem
                              key={item.id}
                              value={item.id.toString()}
                              className="cursor-pointer"
                            >
                              {item.name} ({formatCurrency(item.price)})
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 items-start">
                <FormField
                  control={form.control}
                  name="orderVolume"
                  render={({ field }) => (
                    <FormItem>
                      <Label className="font-semibold">
                        Số lượng / Khối lượng (kg)
                      </Label>
                      <input
                        type="number"
                        min={0.5}
                        step="0.5"
                        className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                        name={field.name}
                        onBlur={field.onBlur}
                        ref={field.ref}
                        onChange={(e) => {
                          const parsed = parseFloat(e.target.value);
                          field.onChange(Number.isFinite(parsed) ? parsed : 0);
                        }}
                        value={field.value || ""}
                      />
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <div className="rounded-xl border border-amber-200 bg-amber-50/70 px-4 py-3">
                  <div className="flex items-center justify-between text-xs text-gray-600">
                    <span>Đơn giá:</span>
                    <span className="font-semibold text-gray-900">
                      {formatCurrency(selectedPrice)}
                    </span>
                  </div>
                  {selectedServiceObj?.estimatedTime && (
                    <div className="mt-1 flex items-center justify-between text-xs text-gray-600">
                      <span className="inline-flex items-center gap-1">
                        <Clock className="h-3.5 w-3.5" />
                        Thời gian dự kiến:
                      </span>
                      <span className="font-semibold text-gray-900">
                        {selectedServiceObj.estimatedTime}
                      </span>
                    </div>
                  )}
                  <div className="mt-2 flex items-center justify-between border-t border-amber-200 pt-2">
                    <span className="text-sm font-semibold text-gray-800">
                      Tổng tạm tính:
                    </span>
                    <span className="text-lg font-bold text-black">
                      {formatCurrency(totalAmount)}
                    </span>
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <FormField
                  control={form.control}
                  name="phone"
                  render={({ field }) => (
                    <FormItem>
                      <Label className="inline-flex items-center gap-1.5 font-semibold">
                        <Phone className="h-3.5 w-3.5 text-gray-500" />
                        Số điện thoại liên hệ
                      </Label>
                      <input
                        type="tel"
                        placeholder="0901234567"
                        className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                        {...field}
                      />
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="address"
                  render={({ field }) => (
                    <FormItem>
                      <Label className="inline-flex items-center gap-1.5 font-semibold">
                        <MapPin className="h-3.5 w-3.5 text-gray-500" />
                        Địa chỉ giao nhận
                      </Label>
                      <input
                        type="text"
                        placeholder="Số nhà, đường, quận/huyện"
                        className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                        {...field}
                      />
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>

              <FormField
                control={form.control}
                name="instructions"
                render={({ field }) => (
                  <FormItem>
                    <Label className="font-semibold">Ghi chú cho nhân viên</Label>
                    <input
                      type="text"
                      placeholder="Ví dụ: Giao buổi tối sau 18h, không dùng nước xả..."
                      className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                      {...field}
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="imgProduct"
                render={() => (
                  <FormItem>
                    <Label className="font-semibold">
                      Hình ảnh món đồ (tùy chọn)
                    </Label>
                    <div className="flex flex-wrap gap-3 items-center justify-start">
                      <Avatar className="aspect-square w-[68px] h-[68px] rounded-xl object-cover border">
                        <AvatarImage src={previewAvatar} />
                        <AvatarFallback className="rounded-xl text-[11px]">
                          Hình ảnh
                        </AvatarFallback>
                      </Avatar>
                      <input
                        type="file"
                        accept="image/*"
                        className="hidden"
                        ref={avatarInputRef}
                        onChange={handleFileChange}
                      />
                      <button
                        className="flex aspect-square w-[68px] flex-col gap-1 items-center justify-center rounded-xl border border-dashed border-gray-300 hover:border-main cursor-pointer text-[11px] text-gray-500"
                        type="button"
                        onClick={() => avatarInputRef.current?.click()}
                      >
                        <Upload className="h-4 w-4 text-muted-foreground" />
                        <span>Tải ảnh</span>
                      </button>
                      {previewAvatar && (
                        <button
                          type="button"
                          onClick={handleRemoveImage}
                          className="cursor-pointer rounded-lg border border-zinc-200 px-3 py-1.5 text-xs font-medium text-zinc-600 hover:bg-zinc-100"
                        >
                          Xóa ảnh
                        </button>
                      )}
                    </div>
                  </FormItem>
                )}
              />

              {isNonCustomer && (
                <p className="rounded-lg bg-amber-50 border border-amber-200 px-3 py-2 text-sm text-amber-800">
                  Chỉ tài khoản Khách hàng mới có thể đặt lịch dịch vụ.
                </p>
              )}

              <div className="flex flex-col-reverse sm:flex-row items-center gap-2.5 pt-2">
                <button
                  type="button"
                  onClick={handleClose}
                  className="w-full sm:w-auto sm:min-w-[120px] cursor-pointer rounded-[28px] border border-zinc-300 bg-white px-5 py-2.5 sm:py-3 text-sm sm:text-base font-semibold text-zinc-700 hover:bg-zinc-100 transition-colors"
                >
                  Đóng
                </button>
                <button
                  type="submit"
                  disabled={orderMutation.isPending || isNonCustomer}
                  className="w-full flex-1 cursor-pointer flex justify-center rounded-[28px] bg-main px-5 py-2.5 sm:py-3 text-sm sm:text-base font-bold text-black shadow-xs hover:bg-hover transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {orderMutation.isPending
                    ? "Đang tạo đơn hàng..."
                    : "Xác nhận đặt lịch & Thanh toán"}
                </button>
              </div>
            </form>
          </Form>
        </div>
      </DialogContent>
    </Dialog>
  );
}
