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
import { Upload } from "lucide-react";

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

export default function Order() {
  const { openOrder, setOpenOrder, isAuth } = useAuthStore();
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

  const orderMutation = useOrderMutation();

  const form = useForm<OrderType>({
    resolver: zodResolver(OrderSchema),
    defaultValues: {
      laundryShop: { id: 0 },
      serviceCategory: { id: 0 },
      service: { id: 0 },
      orderVolume: 1,
      address: "",
      instructions: "",
      imgProduct: "",
    },
  });

  useEffect(() => {
    if (openOrder && account?.address && !form.getValues("address")) {
      form.setValue("address", account.address);
    }
  }, [openOrder, account?.address, form]);

  const watchedVolume = useWatch({
    control: form.control,
    name: "orderVolume",
  });
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
      address: account?.address ?? "",
      instructions: "",
      imgProduct: "",
    });
    setShopId(0);
    setCategoryId(0);
    setSelectedPrice(0);
    setPreviewAvatar("");
    if (avatarInputRef.current) {
      avatarInputRef.current.value = "";
    }
  };

  const onSubmit = async (values: OrderType) => {
    if (orderMutation.isPending) return;
    try {
      const result = await orderMutation.mutateAsync(values);
      setOpenOrder(false);
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
          reset();
        }
        setOpenOrder(open);
      }}
    >
      <DialogContent
        className="border-none w-full max-w-lg sm:max-w-xl md:max-w-2xl"
        aria-describedby={undefined}
        onInteractOutside={(e) => {
          e.preventDefault();
        }}
      >
        <div className="flex min-h-full flex-col justify-center px-6 py-8 lg:px-8">
          <DialogTitle className="z-500 sm:mx-auto sm:w-full sm:max-w-sm">
            <div className="mb-5 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
              Đăng ký dịch vụ
            </div>
          </DialogTitle>
          <Form {...form}>
            <form
              className="space-y-3"
              noValidate
              onSubmit={form.handleSubmit(onSubmit)}
              onReset={reset}
            >
              <FormField
                control={form.control}
                name="laundryShop.id"
                render={({ field }) => (
                  <FormItem>
                    <Label>Chọn cửa hàng</Label>
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

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-10">
                <FormField
                  control={form.control}
                  name="serviceCategory.id"
                  render={({ field }) => (
                    <FormItem>
                      <Label>Loại hình dịch vụ</Label>
                      <Select
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
                      <Label>Dịch vụ</Label>
                      <Select
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

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-10">
                <FormField
                  control={form.control}
                  name="orderVolume"
                  render={({ field }) => (
                    <FormItem>
                      <div>
                        <label className="block text-sm/6 font-medium text-gray-900">
                          Khối lượng (kg)
                        </label>
                        <div className="mt-2">
                          <input
                            type="number"
                            min={0.1}
                            step="0.5"
                            className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                            name={field.name}
                            onBlur={field.onBlur}
                            ref={field.ref}
                            onChange={(e) => {
                              const parsed = parseFloat(e.target.value);
                              field.onChange(
                                Number.isFinite(parsed) ? parsed : 0
                              );
                            }}
                            value={field.value || ""}
                          />
                        </div>
                        <FormMessage />
                      </div>
                    </FormItem>
                  )}
                />

                <div>
                  <label className="block text-sm/6 font-medium text-gray-900">
                    Tổng tiền (VND)
                  </label>
                  <div className="mt-2">
                    <input
                      type="text"
                      disabled
                      value={formatCurrency(totalAmount)}
                      className="block w-full rounded-md bg-gray-50 px-3 py-1.5 text-base font-medium text-gray-900 outline-1 -outline-offset-1 outline-gray-300 sm:text-sm/6"
                    />
                  </div>
                </div>
              </div>

              <FormField
                control={form.control}
                name="address"
                render={({ field }) => (
                  <FormItem>
                    <div>
                      <label className="block text-sm/6 font-medium text-gray-900">
                        Địa chỉ giao nhận
                      </label>
                      <div className="mt-2">
                        <input
                          type="text"
                          placeholder="Nhập địa chỉ nhận và trả đồ"
                          className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                          {...field}
                        />
                      </div>
                      <FormMessage />
                    </div>
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="instructions"
                render={({ field }) => (
                  <FormItem>
                    <div>
                      <label className="block text-sm/6 font-medium text-gray-900">
                        Ghi chú
                      </label>
                      <div className="mt-2">
                        <input
                          type="text"
                          className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                          {...field}
                        />
                      </div>
                      <FormMessage />
                    </div>
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="imgProduct"
                render={() => (
                  <FormItem>
                    <Label htmlFor="name">Hình ảnh đơn hàng</Label>
                    <div className="flex gap-2 items-start justify-start">
                      <Avatar className="aspect-square w-[100px] h-[100px] rounded-md object-cover">
                        <AvatarImage src={previewAvatar} />
                        <AvatarFallback className="rounded-none">
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
                        className="flex aspect-square w-[100px] items-center justify-center rounded-md border border-dashed cursor-pointer"
                        type="button"
                        onClick={() => avatarInputRef.current?.click()}
                      >
                        <Upload className="h-4 w-4 text-muted-foreground" />
                        <span className="sr-only">Upload</span>
                      </button>
                    </div>
                  </FormItem>
                )}
              />

              <div>
                <button
                  disabled={orderMutation.isPending}
                  className="cursor-pointer flex w-full justify-center rounded-md bg-main px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {orderMutation.isPending
                    ? "Đang tạo đơn hàng..."
                    : "Đăng ký dịch vụ"}
                </button>
              </div>
            </form>
          </Form>
        </div>
      </DialogContent>
    </Dialog>
  );
}
