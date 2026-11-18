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
  const [selectedPrice, setSelectedPrice] = useState<number>(0);

  const router = useRouter();

  const { data: shopsData, isLoading: isLoadingShops } = useShops(
    isAuth && openOrder
  );
  const allShops = shopsData?.payload.data ?? [];

  const { data: categoriesData, isFetching: isFetchingCategories } =
    useShopCategories(shopId);
  const categories = categoriesData?.payload.data ?? [];

  const { data: servicesData, isFetching: isFetchingServices } =
    useShopServices(shopId, categoryId);
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

  // Tự động chọn cửa hàng đầu tiên khi mở popup mà chưa có cửa hàng nào được chọn
  useEffect(() => {
    if (openOrder && allShops.length > 0 && shopId === 0) {
      const firstShopId = allShops[0].id;
      setShopId(firstShopId);
      form.setValue("laundryShop.id", firstShopId);
    }
  }, [openOrder, allShops, shopId, form]);

  // Tự động chọn loại dịch vụ theo preset (nếu đi từ trang /dich-vu/*) hoặc phần tử đầu tiên
  useEffect(() => {
    if (
      !openOrder ||
      shopId === 0 ||
      isFetchingCategories ||
      categories.length === 0
    ) {
      return;
    }
    const currentCatId = form.getValues("serviceCategory.id");
    const existsInShop = categories.some((c) => c.id === currentCatId);
    if (currentCatId > 0 && existsInShop) {
      return;
    }
    const matched =
      (preferredCategory &&
        categories.find((c) =>
          matchesPreference(c.name, preferredCategory)
        )) ||
      categories[0];
    if (matched) {
      setCategoryId(matched.id);
      setSelectedPrice(0);
      form.setValue("serviceCategory.id", matched.id);
      form.setValue("service.id", 0);
    }
  }, [
    openOrder,
    shopId,
    isFetchingCategories,
    categories,
    preferredCategory,
    form,
  ]);

  // Tự động chọn dịch vụ cụ thể theo preset hoặc dịch vụ đầu tiên
  useEffect(() => {
    if (
      !openOrder ||
      categoryId === 0 ||
      isFetchingServices ||
      services.length === 0
    ) {
      return;
    }
    const currentSvcId = form.getValues("service.id");
    const currentSvc = services.find((s) => s.id === currentSvcId);
    if (currentSvc) {
      setSelectedPrice(currentSvc.price);
      return;
    }
    const matched =
      (preferredService &&
        services.find((s) => matchesPreference(s.name, preferredService))) ||
      services[0];
    if (matched) {
      form.setValue("service.id", matched.id);
      setSelectedPrice(matched.price);
    }
  }, [
    openOrder,
    categoryId,
    isFetchingServices,
    services,
    preferredService,
    form,
  ]);

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

  const watchedVolume = useWatch({
    control: form.control,
    name: "orderVolume",
  });
  const watchedServiceId = useWatch({
    control: form.control,
    name: "service.id",
  });
  const selectedServiceObj = services.find((s) => s.id === watchedServiceId);
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
    } catch (error: any) {
      toast.error(error?.message || "Không thể xử lý hình ảnh");
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
    setSelectedPrice(0);
    setPreviewAvatar("");
  };

  const onSubmit = async (values: OrderType) => {
    if (orderMutation.isPending || isNonCustomer) return;
    try {
      const result = await orderMutation.mutateAsync(values);
      setOpenOrder(false);
      clearBookingPreset();
      reset();
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
          clearBookingPreset();
          reset();
          if (avatarInputRef.current) {
            avatarInputRef.current.value = "";
          }
        }
        setOpenOrder(open);
      }}
    >
      <DialogContent
        className="border-none w-full max-w-lg sm:max-w-xl md:max-w-2xl p-0 overflow-hidden max-h-[90vh] overflow-y-auto rounded-[28px]"
        aria-describedby={undefined}
        onInteractOutside={(e) => {
          e.preventDefault();
        }}
      >
        <div className="bg-black px-6 py-5 text-white">
          <div className="inline-flex items-center gap-1.5 rounded-full bg-main px-3 py-1 text-xs font-bold text-black mb-2">
            <Sparkles className="h-3.5 w-3.5" />
            HERAMO • ĐẶT DỊCH VỤ 4.0
          </div>
          <DialogTitle className="text-2xl font-bold text-white">
            Đặt lịch giặt ủi & vệ sinh
          </DialogTitle>
          <p className="mt-1 text-sm text-gray-300">
            Chọn cửa hàng, dịch vụ và địa chỉ giao nhận — thanh toán nhanh bằng
            VietQR.
          </p>
        </div>

        <div className="px-6 py-6">
          <Form {...form}>
            <form
              className="space-y-4"
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
                      value={field.value ? String(field.value) : ""}
                      onValueChange={(value) => {
                        const nextShopId = parseInt(value, 10);
                        field.onChange(nextShopId);
                        setShopId(nextShopId);
                        setCategoryId(0);
                        setSelectedPrice(0);
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

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <FormField
                  control={form.control}
                  name="serviceCategory.id"
                  render={({ field }) => (
                    <FormItem>
                      <Label className="font-semibold">Loại hình dịch vụ</Label>
                      <Select
                        key={`category-${shopId}`}
                        disabled={!shopId || isFetchingCategories}
                        value={field.value ? String(field.value) : ""}
                        onValueChange={(value) => {
                          const nextCategoryId = parseInt(value, 10);
                          field.onChange(nextCategoryId);
                          setCategoryId(nextCategoryId);
                          setSelectedPrice(0);
                          form.setValue("service.id", 0);
                        }}
                      >
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue
                              placeholder={
                                !shopId
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
                            shopId > 0 &&
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
                        key={`service-${shopId}-${categoryId}`}
                        disabled={!categoryId || isFetchingServices}
                        value={field.value ? String(field.value) : ""}
                        onValueChange={(value) => {
                          const nextServiceId = parseInt(value, 10);
                          field.onChange(nextServiceId);
                          const selectedService = services.find(
                            (service) => service.id === nextServiceId
                          );
                          setSelectedPrice(selectedService?.price ?? 0);
                        }}
                      >
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue
                              placeholder={
                                !categoryId
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
                            categoryId > 0 &&
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
                    <div className="flex gap-3 items-center justify-start">
                      <Avatar className="aspect-square w-[84px] h-[84px] rounded-xl object-cover border">
                        <AvatarImage src={previewAvatar} />
                        <AvatarFallback className="rounded-xl text-xs">
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
                        className="flex aspect-square w-[84px] flex-col gap-1 items-center justify-center rounded-xl border border-dashed border-gray-300 hover:border-main cursor-pointer text-xs text-gray-500"
                        type="button"
                        onClick={() => avatarInputRef.current?.click()}
                      >
                        <Upload className="h-4 w-4 text-muted-foreground" />
                        <span>Tải ảnh</span>
                      </button>
                    </div>
                  </FormItem>
                )}
              />

              {isNonCustomer && (
                <p className="rounded-lg bg-amber-50 border border-amber-200 px-3 py-2 text-sm text-amber-800">
                  Chỉ tài khoản Khách hàng mới có thể đặt lịch dịch vụ.
                </p>
              )}

              <div className="pt-2">
                <button
                  disabled={orderMutation.isPending || isNonCustomer}
                  className="cursor-pointer flex w-full justify-center rounded-[28px] bg-main px-5 py-3 text-base font-bold text-black shadow-xs hover:bg-hover transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
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
