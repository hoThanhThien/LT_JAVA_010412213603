"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";
import {
  ArrowLeft,
  CheckCircle2,
  Eye,
  EyeOff,
  KeyRound,
  Mail,
  RefreshCw,
} from "lucide-react";

import { Form, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { Label } from "@/components/ui/label";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import {
  useForgotPasswordMutation,
  useResetPasswordMutation,
} from "@/queries/useAuth";
import {
  ForgotPasswordBody,
  ForgotPasswordBodyType,
  ResetPasswordBody,
  ResetPasswordBodyType,
} from "@/schemaValidations/auth.schema";
import { handleErrorApi } from "@/lib/utils";

interface ForgotPasswordProps {
  defaultIdentifier?: string;
  onSuccess?: () => void;
  onBack?: () => void;
  backLabel?: string;
  compact?: boolean;
}

export default function ForgotPassword({
  defaultIdentifier = "",
  onSuccess,
  onBack,
  backLabel = "Quay lại đăng nhập",
  compact = false,
}: ForgotPasswordProps) {
  const [step, setStep] = useState<"request" | "reset">("request");
  const [identifier, setIdentifier] = useState(defaultIdentifier);
  const [deliveryMeta, setDeliveryMeta] = useState<{
    emailSent: boolean;
    maskedEmail?: string | null;
    devOtp?: string | null;
  } | null>(null);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const forgotMutation = useForgotPasswordMutation();
  const resetMutation = useResetPasswordMutation();

  const requestForm = useForm<ForgotPasswordBodyType>({
    resolver: zodResolver(ForgotPasswordBody),
    defaultValues: {
      identifier: defaultIdentifier,
    },
  });

  const resetForm = useForm<ResetPasswordBodyType>({
    resolver: zodResolver(ResetPasswordBody),
    defaultValues: {
      identifier: defaultIdentifier,
      otp: "",
      newPassword: "",
      confirmPassword: "",
    },
  });

  const handleRequestOtp = async (values: ForgotPasswordBodyType) => {
    if (forgotMutation.isPending) return;
    try {
      const cleanId = values.identifier.trim();
      const res = await forgotMutation.mutateAsync({ identifier: cleanId });
      const data = res.payload.data;

      setIdentifier(cleanId);
      setDeliveryMeta({
        emailSent: Boolean(data?.emailSent),
        maskedEmail: data?.maskedEmail ?? null,
        devOtp: data?.devOtp ?? null,
      });
      resetForm.reset({
        identifier: cleanId,
        otp: "",
        newPassword: "",
        confirmPassword: "",
      });
      setStep("reset");
      toast.success(res.payload.message, {
        position: "bottom-left",
        autoClose: 3500,
      });
    } catch (error) {
      handleErrorApi({
        error,
        setError: requestForm.setError,
      });
    }
  };

  const handleResendOtp = async () => {
    if (forgotMutation.isPending || !identifier) return;
    try {
      const res = await forgotMutation.mutateAsync({ identifier });
      const data = res.payload.data;
      setDeliveryMeta({
        emailSent: Boolean(data?.emailSent),
        maskedEmail: data?.maskedEmail ?? null,
        devOtp: data?.devOtp ?? null,
      });
      toast.success(res.payload.message, {
        position: "bottom-left",
        autoClose: 3500,
      });
    } catch (error) {
      handleErrorApi({ error });
    }
  };

  const handleResetPassword = async (values: ResetPasswordBodyType) => {
    if (resetMutation.isPending) return;
    try {
      const res = await resetMutation.mutateAsync({
        ...values,
        identifier: identifier || values.identifier,
      });
      toast.success(res.payload.message, {
        position: "bottom-left",
        autoClose: 3500,
      });
      setStep("request");
      setDeliveryMeta(null);
      resetForm.reset({
        identifier: defaultIdentifier,
        otp: "",
        newPassword: "",
        confirmPassword: "",
      });
      onSuccess?.();
    } catch (error) {
      handleErrorApi({
        error,
        setError: resetForm.setError,
      });
    }
  };

  return (
    <div
      className={
        compact
          ? "space-y-4"
          : "flex min-h-full flex-col justify-center px-6 py-8 lg:px-8"
      }
    >
      {!compact && (
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <div className="mx-auto mb-3 flex h-12 w-12 items-center justify-center rounded-2xl bg-[#111111] text-[#ffbb00] shadow-sm">
            <KeyRound className="h-6 w-6" />
          </div>
          <h2 className="text-center text-2xl/8 font-bold tracking-tight text-gray-900">
            Khôi phục mật khẩu
          </h2>
          <p className="mt-1 text-center text-xs text-zinc-500">
            {step === "request"
              ? "Nhập email hoặc số điện thoại để nhận mã xác thực OTP."
              : "Nhập mã OTP 6 chữ số và mật khẩu mới của bạn."}
          </p>
        </div>
      )}

      <div className={compact ? "w-full" : "mt-6 sm:mx-auto sm:w-full sm:max-w-sm"}>
        {step === "request" ? (
          <Form {...requestForm}>
            <form
              onSubmit={requestForm.handleSubmit(handleRequestOtp)}
              className="space-y-4"
              noValidate
            >
              <FormField
                control={requestForm.control}
                name="identifier"
                render={({ field }) => (
                  <FormItem>
                    <Label
                      htmlFor="forgot-identifier"
                      className="block text-sm font-medium text-gray-900"
                    >
                      Email hoặc Số điện thoại
                    </Label>
                    <div className="relative mt-1.5">
                      <Mail className="pointer-events-none absolute top-1/2 left-3 h-4 w-4 -translate-y-1/2 text-zinc-400" />
                      <input
                        id="forgot-identifier"
                        type="text"
                        placeholder="example@gmail.com hoặc 0901234567"
                        className="block w-full rounded-lg border border-zinc-200 bg-white py-2 pr-3 pl-9 text-sm text-gray-900 outline-1 -outline-offset-1 outline-gray-200 placeholder:text-gray-400 focus:border-[#ffbb00] focus:ring-2 focus:ring-[#ffbb00]/30 focus:outline-none"
                        {...field}
                      />
                    </div>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <button
                type="submit"
                disabled={forgotMutation.isPending}
                className="flex w-full cursor-pointer items-center justify-center gap-2 rounded-lg bg-[#111111] px-4 py-2.5 text-sm font-semibold text-[#ffbb00] shadow-xs transition hover:bg-zinc-800 disabled:cursor-not-allowed disabled:opacity-60"
              >
                {forgotMutation.isPending ? "Đang gửi mã OTP..." : "Gửi mã OTP khôi phục"}
              </button>
            </form>
          </Form>
        ) : (
          <div className="space-y-4">
            <div className="rounded-xl border border-amber-200 bg-amber-50/70 p-3 text-xs text-zinc-700">
              <div className="flex items-start gap-2">
                <CheckCircle2 className="mt-0.5 h-4 w-4 shrink-0 text-amber-600" />
                <div className="space-y-1">
                  <p className="font-semibold text-zinc-900">
                    {deliveryMeta?.emailSent && deliveryMeta.maskedEmail
                      ? `OTP đã được gửi đến ${deliveryMeta.maskedEmail}`
                      : "Đã tạo mã OTP khôi phục (hiệu lực 5 phút)"}
                  </p>
                  <p className="text-zinc-600">
                    Tài khoản: <span className="font-medium text-zinc-900">{identifier}</span>
                  </p>
                  {deliveryMeta?.devOtp && (
                    <div className="mt-2 flex flex-wrap items-center gap-2 rounded-lg border border-amber-300 bg-white px-2.5 py-1.5">
                      <span className="text-[11px] font-medium text-zinc-600">
                        Mã OTP của bạn:
                      </span>
                      <span className="font-mono text-sm font-bold tracking-widest text-[#111111]">
                        {deliveryMeta.devOtp}
                      </span>
                      <button
                        type="button"
                        onClick={() =>
                          resetForm.setValue("otp", deliveryMeta.devOtp ?? "", {
                            shouldValidate: true,
                          })
                        }
                        className="ml-auto cursor-pointer rounded-md bg-[#ffbb00] px-2 py-0.5 text-[11px] font-semibold text-[#111111] hover:bg-[#e5a800]"
                      >
                        Điền nhanh
                      </button>
                    </div>
                  )}
                </div>
              </div>
            </div>

            <Form {...resetForm}>
              <form
                onSubmit={resetForm.handleSubmit(handleResetPassword)}
                className="space-y-3.5"
                noValidate
              >
                <FormField
                  control={resetForm.control}
                  name="otp"
                  render={({ field }) => (
                    <FormItem>
                      <div className="flex items-center justify-between">
                        <Label className="text-sm font-medium text-gray-900">
                          Mã OTP (6 chữ số)
                        </Label>
                        <button
                          type="button"
                          onClick={handleResendOtp}
                          disabled={forgotMutation.isPending}
                          className="inline-flex cursor-pointer items-center gap-1 text-xs font-semibold text-amber-700 hover:text-amber-800 disabled:opacity-50"
                        >
                          <RefreshCw className="h-3 w-3" />
                          {forgotMutation.isPending ? "Đang gửi..." : "Gửi lại OTP"}
                        </button>
                      </div>
                      <div className="mt-1 flex justify-center">
                        <InputOTP
                          maxLength={6}
                          value={field.value}
                          onChange={field.onChange}
                        >
                          <InputOTPGroup>
                            <InputOTPSlot index={0} />
                            <InputOTPSlot index={1} />
                            <InputOTPSlot index={2} />
                            <InputOTPSlot index={3} />
                            <InputOTPSlot index={4} />
                            <InputOTPSlot index={5} />
                          </InputOTPGroup>
                        </InputOTP>
                      </div>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={resetForm.control}
                  name="newPassword"
                  render={({ field }) => (
                    <FormItem>
                      <Label
                        htmlFor="forgot-new-password"
                        className="block text-sm font-medium text-gray-900"
                      >
                        Mật khẩu mới
                      </Label>
                      <div className="relative mt-1">
                        <input
                          id="forgot-new-password"
                          type={showNewPassword ? "text" : "password"}
                          placeholder="Từ 8 ký tự, chữ, số & ký tự đặc biệt"
                          className="block w-full rounded-lg border border-zinc-200 bg-white py-2 pr-10 pl-3 text-sm text-gray-900 placeholder:text-gray-400 focus:border-[#ffbb00] focus:ring-2 focus:ring-[#ffbb00]/30 focus:outline-none"
                          {...field}
                        />
                        <button
                          type="button"
                          onClick={() => setShowNewPassword((prev) => !prev)}
                          className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer text-zinc-400 hover:text-zinc-600"
                          aria-label={showNewPassword ? "Ẩn mật khẩu" : "Hiện mật khẩu"}
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
                  control={resetForm.control}
                  name="confirmPassword"
                  render={({ field }) => (
                    <FormItem>
                      <Label
                        htmlFor="forgot-confirm-password"
                        className="block text-sm font-medium text-gray-900"
                      >
                        Xác nhận mật khẩu mới
                      </Label>
                      <div className="relative mt-1">
                        <input
                          id="forgot-confirm-password"
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

                <div className="flex items-center justify-between gap-2 pt-1">
                  <button
                    type="button"
                    onClick={() => setStep("request")}
                    className="cursor-pointer rounded-lg border border-zinc-200 px-3 py-2 text-xs font-medium text-zinc-600 hover:bg-zinc-50"
                  >
                    Đổi email / SĐT
                  </button>
                  <button
                    type="submit"
                    disabled={resetMutation.isPending}
                    className="flex-1 cursor-pointer rounded-lg bg-[#ffbb00] px-4 py-2 text-sm font-bold text-[#111111] shadow-xs transition hover:bg-[#e5a800] disabled:cursor-not-allowed disabled:opacity-60"
                  >
                    {resetMutation.isPending
                      ? "Đang cập nhật..."
                      : "Xác nhận đặt lại mật khẩu"}
                  </button>
                </div>
              </form>
            </Form>
          </div>
        )}

        {onBack && (
          <div className="mt-5 text-center">
            <button
              type="button"
              onClick={onBack}
              className="inline-flex cursor-pointer items-center gap-1.5 text-xs font-semibold text-zinc-600 hover:text-[#111111]"
            >
              <ArrowLeft className="h-3.5 w-3.5" />
              {backLabel}
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
