"use client";

import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { useRef, useState } from "react";
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
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Label } from "@/components/ui/label";
import { Upload } from "lucide-react";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { OrderSchema, OrderType } from "@/schemaValidations/order.schema";
import { CategoryType, ServiceType } from "@/schemaValidations/store.schema";
import { useShops } from "@/queries/useShop";
import shopApiRequests from "@/apiRequests/shop";
import { toast } from "react-toastify";
import { handleErrorApi } from "@/lib/utils";
import orderApiRequests from "@/apiRequests/order";
import { useRouter } from "next/navigation";

export default function Order() {
  const { openOrder, setOpenOrder } = useAuthStore();
  const [categories, setCategories] = useState<CategoryType[]>([]);
  const [services, setServices] = useState<ServiceType[]>([]);
  const [shopId, setShopId] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);
  const avatarInputRef = useRef<HTMLInputElement>(null);
  const [previewAvatar, setPreviewAvatar] = useState("");

  const [selectedPrice, setSelectedPrice] = useState<number>(0);
  const [volume, setVolume] = useState<number>(0);

  const router = useRouter();

  const { data } = useShops();
  const allShops = data?.payload.data;

  const form = useForm<OrderType>({
    resolver: zodResolver(OrderSchema),
    defaultValues: {
      orderVolume: 0,
      instructions: "",
      imgProduct: "",
    },
  });

  const convertToBase64 = (file: File) => {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);

      fileReader.onload = () => {
        resolve(fileReader.result);
      };

      fileReader.onerror = (error) => {
        reject(error);
      };
    });
  };

  const handleFileChange = async (e: any) => {
    const file = e.target.files?.[0];
    if (file) {
      try {
        const base64: any = await convertToBase64(file);
        setPreviewAvatar(base64); // Lưu base64 vào state để hiển thị preview
        form.setValue("imgProduct", base64); // Cập nhật giá trị trong form
      } catch (error) {
        console.error("Error converting image to base64:", error);
      }
    }
  };

  // const previewAvatar = file ? URL.createObjectURL(file) : avatar;

  const reset = () => {
    form.reset();
    setPreviewAvatar("");
  };

  const onSubmit = async (data: OrderType) => {
    try {
      setLoading(true);
      const result = await orderApiRequests.customerOrder(data);
      setOpenOrder(false);
      reset();
      setPreviewAvatar("");
      setLoading(false);
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
    <>
      <Dialog
        open={openOrder}
        onOpenChange={(open: boolean) => {
          if (!open) {
            reset(); // Reset form khi dialog đóng
            setPreviewAvatar("");
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
          <div className=" flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
            <DialogTitle className="z-500 sm:mx-auto sm:w-full sm:max-w-sm">
              <div className="mb-5 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
                Đăng ký dịch vụ
              </div>
            </DialogTitle>
            <Form {...form}>
              <form
                className="space-y-3"
                noValidate
                onSubmit={form.handleSubmit(onSubmit, (err) => {
                  console.warn(err);
                })}
                onReset={reset}
              >
                <FormField
                  control={form.control}
                  name="laundryShop.id"
                  render={({ field }) => (
                    <FormItem>
                      <Label>Chọn cửa hàng</Label>
                      <Select
                        onValueChange={async (value: any) => {
                          field.onChange(parseInt(value)); // Fix: Call onChange with the value
                          setShopId(parseInt(value)); // Convert string to number
                          const allCategories =
                            await shopApiRequests.categories(parseInt(value));
                          setCategories(allCategories.payload.data);
                        }}
                      >
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {allShops &&
                            allShops.map((shop) => (
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

                <div className="grid grid-cols-2 gap-10">
                  <FormField
                    control={form.control}
                    name="serviceCategory.id"
                    render={({ field }) => (
                      <FormItem>
                        <Label>Loại hình dịch vụ</Label>
                        <Select
                          onValueChange={async (value: any) => {
                            field.onChange(parseInt(value)); // Fix: Call onChange with the value
                            const allServices = await shopApiRequests.services(
                              shopId,
                              parseInt(value)
                            );
                            setServices(allServices.payload.data);
                          }}
                        >
                          <FormControl>
                            <SelectTrigger className="w-full">
                              <SelectValue />
                            </SelectTrigger>
                          </FormControl>
                          <SelectContent>
                            {categories &&
                              categories.map((item) => (
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
                          onValueChange={async (value: any) => {
                            field.onChange(parseInt(value)); // Fix: Call onChange with the value

                            const selectedService = services.find(
                              (service) => service.id === parseInt(value)
                            );

                            // Lưu giá dịch vụ vào state
                            if (selectedService) {
                              setSelectedPrice(selectedService.price);
                            }
                          }}
                        >
                          <FormControl>
                            <SelectTrigger className="w-full">
                              <SelectValue />
                            </SelectTrigger>
                          </FormControl>
                          <SelectContent>
                            {services &&
                              services.map((item) => (
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
                </div>

                <div className="grid grid-cols-2 gap-10">
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
                              className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                              {...field}
                              onChange={(e) => {
                                field.onChange(Number(e.target.value));
                                setVolume(Number(e.target.value));
                              }}
                              value={field.value}
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
                        type="number"
                        disabled
                        value={selectedPrice * volume}
                        className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                      />
                    </div>
                  </div>
                </div>

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
                          className="flex aspect-square w-[100px] items-center justify-center rounded-md border border-dashed"
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
                    disabled={loading}
                    className="cursor-pointer flex w-full justify-center rounded-md bg-main px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    Đăng ký dịch vụ
                  </button>
                </div>
              </form>
            </Form>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
