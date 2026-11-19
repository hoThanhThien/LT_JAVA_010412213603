"use client";

import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { useEffect, useRef, useState } from "react";
import { useAuthStore } from "@/lib/zustand";
import { Form, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Upload, Eye, EyeOff, KeyRound, ShieldCheck } from "lucide-react";

import {
  ChangePasswordBody,
  ChangePasswordBodyType,
  UpdateMeBody,
  UpdateMeType,
} from "@/schemaValidations/account.schema";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
  useAccountMe,
  useChangePasswordMutation,
  useUpdateMeMutation,
} from "@/queries/useAccount";
import { toast } from "react-toastify";
import { compressImageToBase64, handleErrorApi } from "@/lib/utils";
import ForgotPassword from "@/components/forgot-password";

export default function Setting() {
  const { openSetting, setOpenSetting, isAuth } = useAuthStore();
  const avatarInputRef = useRef<HTMLInputElement>(null);
  const [previewAvatar, setPreviewAvatar] = useState("");
  const [passwordMode, setPasswordMode] = useState<"change" | "forgot">("change");
  const [showCurrentPassword, setShowCurrentPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const { data } = useAccountMe(isAuth && openSetting);
  const account = data?.payload?.data?.account;
  const updateMeMutation = useUpdateMeMutation();
  const changePasswordMutation = useChangePasswordMutation();

  const changePasswordForm = useForm<ChangePasswordBodyType>({
    resolver: zodResolver(ChangePasswordBody),
    defaultValues: {
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
    },
  });

  const form = useForm<UpdateMeType>({
    resolver: zodResolver(UpdateMeBody),
    defaultValues: {
      username: "",
      phone: "",
      address: "",
      email: "",
      avtUser: "",
    },
  });

  useEffect(() => {
    if (account && openSetting) {
      form.reset({
        username: account.username ?? "",
        phone: account.phone ?? "",
        address: account.address ?? "",
        email: account.email ?? "",
        avtUser: account.avtUser ?? "",
      });
    }
  }, [account, openSetting, form]);

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    try {
      const base64 = await compressImageToBase64(file, 800, 0.78);
      setPreviewAvatar(base64);
      form.setValue("avtUser", base64);
    } catch (error) {
      toast.error(
        error instanceof Error ? error.message : "Không thể xử lý ảnh đại diện"
      );
    }
  };

  const onChangePasswordSubmit = async (values: ChangePasswordBodyType) => {
    if (changePasswordMutation.isPending) return;
    const hasPhone = Boolean(account?.phone && account.phone.trim().length > 0);
    if (hasPhone && !values.currentPassword?.trim()) {
      changePasswordForm.setError("currentPassword", {
        type: "manual",
        message: "Vui lòng nhập mật khẩu hiện tại (hoặc dùng Quên mật khẩu qua OTP)",
      });
      return;
    }
    try {
      const res = await changePasswordMutation.mutateAsync({
        currentPassword: values.currentPassword ?? "",
        newPassword: values.newPassword,
        confirmPassword: values.confirmPassword,
      });
      toast.success(res.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
      });
      changePasswordForm.reset({
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
      });
    } catch (error) {
      handleErrorApi({
        error,
        setError: changePasswordForm.setError,
      });
    }
  };

  const reset = () => {
    form.reset();
    setPreviewAvatar("");
  };

  const handleClose = () => {
    setOpenSetting(false);
    reset();
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
      handleClose();
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
          className="border-none w-[calc(100vw-1.5rem)] sm:w-full max-w-lg sm:max-w-xl p-0 flex flex-col max-h-[min(90dvh,740px)] overflow-hidden rounded-2xl sm:rounded-[24px]"
          aria-describedby={undefined}
          onInteractOutside={(e) => {
            if (updateMeMutation.isPending || changePasswordMutation.isPending) {
              e.preventDefault();
            }
          }}
        >
          <DialogTitle className="shrink-0 w-full">
            <div className="bg-[#111111] px-4 py-3.5 pr-14 sm:px-6 sm:py-4 text-left sm:text-center">
              <span className="inline-block rounded-full border border-[#ffbb00]/40 bg-[#ffbb00]/15 px-2.5 py-0.5 text-[10px] font-bold tracking-[0.2em] text-[#ffbb00] uppercase">
                HERAMO Account
              </span>
              <div className="mt-1 text-lg sm:text-xl font-bold tracking-tight text-white">
                Thông tin tài khoản
              </div>
            </div>
          </DialogTitle>
          <div className="flex-1 min-h-0 overflow-y-auto px-4 py-4 sm:px-6 sm:py-5">
            <Tabs defaultValue="account" className="mx-auto w-full">
              <TabsList className="grid w-full grid-cols-2 rounded-xl bg-zinc-100 p-1">
                <TabsTrigger
                  value="account"
                  className="rounded-lg font-medium data-[state=active]:bg-[#111111] data-[state=active]:text-[#ffbb00]"
                >
                  Tài khoản
                </TabsTrigger>
                <TabsTrigger
                  value="password"
                  className="rounded-lg font-medium data-[state=active]:bg-[#111111] data-[state=active]:text-[#ffbb00]"
                >
                  Mật khẩu
                </TabsTrigger>
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
                      name="phone"
                      render={({ field }) => (
                        <FormItem>
                          <div>
                            <label className="block text-sm/6 font-medium text-gray-900">
                              Số điện thoại
                            </label>
                            <div className="mt-2">
                              <input
                                type="tel"
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
                      name="avtUser"
                      render={() => (
                        <FormItem>
                          <Label htmlFor="name">Avatar</Label>
                          <div className="flex gap-2 items-start justify-start">
                            <Avatar className="aspect-square w-[100px] h-[100px] rounded-md object-cover">
                              <AvatarImage
                                src={previewAvatar || account?.avtUser || undefined}
                              />
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

                    <div className="flex flex-col-reverse sm:flex-row gap-2.5 pt-2">
                      <button
                        type="button"
                        onClick={handleClose}
                        className="w-full sm:w-auto sm:min-w-[110px] cursor-pointer rounded-lg border border-zinc-300 bg-white px-4 py-2 text-sm font-semibold text-zinc-700 hover:bg-zinc-100 transition-colors"
                      >
                        Đóng
                      </button>
                      <button
                        type="submit"
                        disabled={updateMeMutation.isPending}
                        className="w-full flex-1 cursor-pointer flex justify-center rounded-lg bg-main px-4 py-2 text-sm font-bold text-black shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                      >
                        {updateMeMutation.isPending
                          ? "Đang cập nhật..."
                          : "Cập nhật thông tin"}
                      </button>
                    </div>
                  </form>
                </Form>
              </TabsContent>
              <TabsContent value="password" className="mt-4 space-y-4">
                <div className="grid grid-cols-2 gap-2 rounded-xl border border-zinc-200 bg-zinc-50 p-1">
                  <button
                    type="button"
                    onClick={() => setPasswordMode("change")}
                    className={`flex cursor-pointer items-center justify-center gap-1.5 rounded-lg px-3 py-2 text-xs font-semibold transition ${
                      passwordMode === "change"
                        ? "bg-[#ffbb00] text-[#111111] shadow-xs"
                        : "text-zinc-600 hover:text-zinc-900"
                    }`}
                  >
                    <ShieldCheck className="h-3.5 w-3.5" />
                    Đổi mật khẩu
                  </button>
                  <button
                    type="button"
                    onClick={() => setPasswordMode("forgot")}
                    className={`flex cursor-pointer items-center justify-center gap-1.5 rounded-lg px-3 py-2 text-xs font-semibold transition ${
                      passwordMode === "forgot"
                        ? "bg-[#ffbb00] text-[#111111] shadow-xs"
                        : "text-zinc-600 hover:text-zinc-900"
                    }`}
                  >
                    <KeyRound className="h-3.5 w-3.5" />
                    Quên mật khẩu OTP
                  </button>
                </div>

                {passwordMode === "change" ? (
                  <Form {...changePasswordForm}>
                    <form
                      onSubmit={changePasswordForm.handleSubmit(onChangePasswordSubmit)}
                      className="space-y-4"
                      noValidate
                    >
                      {!account?.phone && (
                        <div className="rounded-lg border border-amber-200 bg-amber-50/70 px-3 py-2 text-xs text-zinc-700">
                          Tài khoản đăng nhập qua Google chưa gắn số điện thoại — bạn có thể bỏ trống Mật khẩu hiện tại để đặt mật khẩu mới lần đầu.
                        </div>
                      )}

                      <FormField
                        control={changePasswordForm.control}
                        name="currentPassword"
                        render={({ field }) => (
                          <FormItem>
                            <div className="flex items-center justify-between">
                              <Label
                                htmlFor="setting-current-password"
                                className="text-sm font-medium text-gray-900"
                              >
                                Mật khẩu hiện tại
                                {!account?.phone ? " (tùy chọn)" : ""}
                              </Label>
                              <button
                                type="button"
                                onClick={() => setPasswordMode("forgot")}
                                className="cursor-pointer text-xs font-semibold text-amber-700 hover:text-amber-800"
                              >
                                Quên mật khẩu?
                              </button>
                            </div>
                            <div className="relative mt-1">
                              <input
                                id="setting-current-password"
                                type={showCurrentPassword ? "text" : "password"}
                                placeholder={
                                  account?.phone
                                    ? "Nhập mật khẩu hiện tại"
                                    : "Bỏ trống nếu chưa từng đặt mật khẩu"
                                }
                                className="block w-full rounded-lg border border-zinc-200 bg-white py-2 pr-10 pl-3 text-sm text-gray-900 placeholder:text-gray-400 focus:border-[#ffbb00] focus:ring-2 focus:ring-[#ffbb00]/30 focus:outline-none"
                                {...field}
                                value={field.value ?? ""}
                              />
                              <button
                                type="button"
                                onClick={() => setShowCurrentPassword((prev) => !prev)}
                                className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer text-zinc-400 hover:text-zinc-600"
                                aria-label={
                                  showCurrentPassword ? "Ẩn mật khẩu" : "Hiện mật khẩu"
                                }
                              >
                                {showCurrentPassword ? (
                                  <EyeOff className="h-4 w-4" />
                                ) : (
                                  <Eye className="h-4 w-4" />
                                )}
                              </button>
                            </div>
                            <FormMessage />
                          </FormItem>
                        )}
                      />

                      <FormField
                        control={changePasswordForm.control}
                        name="newPassword"
                        render={({ field }) => (
                          <FormItem>
                            <Label
                              htmlFor="setting-new-password"
                              className="block text-sm font-medium text-gray-900"
                            >
                              Mật khẩu mới
                            </Label>
                            <div className="relative mt-1">
                              <input
                                id="setting-new-password"
                                type={showNewPassword ? "text" : "password"}
                                placeholder="Từ 8 ký tự, gồm chữ, số và ký tự đặc biệt"
                                className="block w-full rounded-lg border border-zinc-200 bg-white py-2 pr-10 pl-3 text-sm text-gray-900 placeholder:text-gray-400 focus:border-[#ffbb00] focus:ring-2 focus:ring-[#ffbb00]/30 focus:outline-none"
                                {...field}
                              />
                              <button
                                type="button"
                                onClick={() => setShowNewPassword((prev) => !prev)}
                                className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer text-zinc-400 hover:text-zinc-600"
                                aria-label={
                                  showNewPassword ? "Ẩn mật khẩu" : "Hiện mật khẩu"
                                }
                              >
                                {showNewPassword ? (
                                  <EyeOff className="h-4 w-4" />
                                ) : (
                                  <Eye className="h-4 w-4" />
                                )}
                              </button>
                            </div>
                            <FormMessage />
                          </FormItem>
                        )}
                      />

                      <FormField
                        control={changePasswordForm.control}
                        name="confirmPassword"
                        render={({ field }) => (
                          <FormItem>
                            <Label
                              htmlFor="setting-confirm-password"
                              className="block text-sm font-medium text-gray-900"
                            >
                              Xác nhận mật khẩu mới
                            </Label>
                            <div className="relative mt-1">
                              <input
                                id="setting-confirm-password"
                                type={showConfirmPassword ? "text" : "password"}
                                placeholder="Nhập lại mật khẩu mới"
                                className="block w-full rounded-lg border border-zinc-200 bg-white py-2 pr-10 pl-3 text-sm text-gray-900 placeholder:text-gray-400 focus:border-[#ffbb00] focus:ring-2 focus:ring-[#ffbb00]/30 focus:outline-none"
                                {...field}
                              />
                              <button
                                type="button"
                                onClick={() => setShowConfirmPassword((prev) => !prev)}
                                className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer text-zinc-400 hover:text-zinc-600"
                                aria-label={
                                  showConfirmPassword ? "Ẩn mật khẩu" : "Hiện mật khẩu"
                                }
                              >
                                {showConfirmPassword ? (
                                  <EyeOff className="h-4 w-4" />
                                ) : (
                                  <Eye className="h-4 w-4" />
                                )}
                              </button>
                            </div>
                            <FormMessage />
                          </FormItem>
                        )}
                      />

                      <div className="flex flex-col-reverse sm:flex-row gap-2.5 pt-1">
                        <button
                          type="button"
                          onClick={handleClose}
                          className="w-full sm:w-auto sm:min-w-[110px] cursor-pointer rounded-lg border border-zinc-300 bg-white px-4 py-2.5 text-sm font-semibold text-zinc-700 hover:bg-zinc-100 transition-colors"
                        >
                          Đóng
                        </button>
                        <button
                          type="submit"
                          disabled={changePasswordMutation.isPending}
                          className="w-full flex-1 flex cursor-pointer justify-center rounded-lg bg-[#111111] px-4 py-2.5 text-sm font-semibold text-[#ffbb00] shadow-xs transition hover:bg-zinc-800 disabled:cursor-not-allowed disabled:opacity-60"
                        >
                          {changePasswordMutation.isPending
                            ? "Đang cập nhật mật khẩu..."
                            : "Cập nhật mật khẩu"}
                        </button>
                      </div>
                    </form>
                  </Form>
                ) : (
                  <ForgotPassword
                    defaultIdentifier={account?.email || account?.phone || ""}
                    compact
                    onSuccess={() => setPasswordMode("change")}
                    onBack={() => setPasswordMode("change")}
                    backLabel="Quay lại đổi mật khẩu"
                  />
                )}
              </TabsContent>
            </Tabs>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
