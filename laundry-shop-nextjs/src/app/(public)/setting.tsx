"use client";

import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { useEffect, useRef, useState } from "react";
import { useAuthStore } from "@/lib/zustand";
import { Form, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Upload } from "lucide-react";

import { UpdateMeBody, UpdateMeType } from "@/schemaValidations/account.schema";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { useAccountMe, useUpdateMeMutation } from "@/queries/useAccount";
import { toast } from "react-toastify";
import { handleErrorApi } from "@/lib/utils";

export default function Setting() {
  const { openSetting, setOpenSetting } = useAuthStore();
  const avatarInputRef = useRef<HTMLInputElement>(null);
  const [previewAvatar, setPreviewAvatar] = useState("");
  const updateMeMutation = useUpdateMeMutation();

  const form = useForm<UpdateMeType>({
    resolver: zodResolver(UpdateMeBody),
    defaultValues: {
      username: "",
      address: "",
      email: "",
    },
  });

  useEffect(() => {
    try {
      const { data } = useAccountMe();
      const account = data?.payload?.data?.account;
      if (account) {
        form.reset({
          username: account.username,
          address: account.address || "", // Assuming address exists in your account data
          email: account.email,
        });
        // setPreviewAvatar(account.avtUser!);
      }
    } catch (error) {
      console.log(error);
    }
  }, [form]);

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
        form.setValue("avt_user", base64); // Cập nhật giá trị trong form
      } catch (error) {
        console.error("Error converting image to base64:", error);
      }
    }
  };

  const reset = () => {
    form.reset();
    setPreviewAvatar("");
  };

  const onSubmit = async (values: UpdateMeType) => {
    if (updateMeMutation.isPending) return;
    try {
      const result = await updateMeMutation.mutateAsync(values);
      toast.success(result.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      setOpenSetting(false);
    } catch (error) {
      handleErrorApi({
        error,
        setError: form.setError,
      });
    }
  };

  return (
    <>
      <Dialog
        open={openSetting}
        onOpenChange={(open: boolean) => {
          setOpenSetting(open);
          if (!open) {
            reset();
          }
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
                Thông tin tài khoản
              </div>
            </DialogTitle>
            <Tabs defaultValue="account" className="mx-auto w-full">
              <TabsList className="grid w-full grid-cols-2">
                <TabsTrigger value="account">Tài khoản</TabsTrigger>
                <TabsTrigger value="pasword">Mật khẩu</TabsTrigger>
              </TabsList>
              <TabsContent value="account">
                <Form {...form}>
                  <form
                    className="space-y-3"
                    noValidate
                    onSubmit={form.handleSubmit(onSubmit, (err) => {
                      console.warn(err);
                    })}
                  >
                    <FormField
                      control={form.control}
                      name="username"
                      render={({ field }) => (
                        <FormItem>
                          <div>
                            <label className="block text-sm/6 font-medium text-gray-900">
                              Username
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
                      name="email"
                      render={({ field }) => (
                        <FormItem>
                          <div>
                            <label className="block text-sm/6 font-medium text-gray-900">
                              Email
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
                      name="address"
                      render={({ field }) => (
                        <FormItem>
                          <div>
                            <label className="block text-sm/6 font-medium text-gray-900">
                              Địa chỉ
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
                      name="avt_user"
                      render={({ field }) => (
                        <FormItem>
                          <Label htmlFor="name">Avatar</Label>
                          <div className="flex gap-2 items-start justify-start">
                            <Avatar className="aspect-square w-[100px] h-[100px] rounded-md object-cover">
                              <AvatarImage src={previewAvatar} />
                              <AvatarFallback className="rounded-none">
                                Avatar
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
                        disabled={updateMeMutation.isPending}
                        className="cursor-pointer flex w-full justify-center rounded-md bg-main px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        Cập nhập thông tin
                      </button>
                    </div>
                  </form>
                </Form>
              </TabsContent>
              <TabsContent value="pasword">ahihi</TabsContent>
            </Tabs>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
