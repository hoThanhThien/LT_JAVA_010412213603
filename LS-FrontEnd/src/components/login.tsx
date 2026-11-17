"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { LoginBody, LoginBodyType } from "@/schemaValidations/auth.schema";
import { Form, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { useGoogleLoginMutation, useLoginMutation } from "@/queries/useAuth";
import { handleErrorApi } from "@/lib/utils";
import { toast } from "react-toastify";
import { useAuthStore } from "@/lib/zustand";
import { useRouter, useSearchParams } from "next/navigation";
import { GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import { auth, googleProvider } from "../../firebase";

export default function Login(props: {
  openAuth: boolean;
  setOpenAuth: (open: boolean) => void;
  setActiveTab: (tab: string) => void;
}) {
  const loginMutation = useLoginMutation();
  const googleLoginMutation = useGoogleLoginMutation();
  const router = useRouter();
  const searchParams = useSearchParams();
  const redirect = searchParams.get("redirect");
  const { setIsAuth } = useAuthStore();
  const form = useForm<LoginBodyType>({
    resolver: zodResolver(LoginBody),
    defaultValues: {
      phone: "",
      password: "",
    },
  });

  const onSubmit = async (data: LoginBodyType) => {
    if (loginMutation.isPending) return;
    try {
      const result = await loginMutation.mutateAsync(data);
      props.setOpenAuth(false);
      setIsAuth(true);
      toast.success(result.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      if (redirect) router.push(redirect);
    } catch (error: any) {
      handleErrorApi({ error, setError: form.setError });
    }
  };

  const handleGoogleLogin = async () => {
    if (googleLoginMutation.isPending) return;
    try {
      const result = await signInWithPopup(auth, googleProvider);
      const idToken = await result.user.getIdToken();
      const credential = GoogleAuthProvider.credentialFromResult(result);
      const res = await googleLoginMutation.mutateAsync({
        idToken,
        googleIdToken: credential?.idToken ?? null,
      });
      props.setOpenAuth(false);
      setIsAuth(true);
      toast.success(res.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      if (redirect) router.push(redirect);
    } catch (error: unknown) {
      const firebaseCode =
        typeof error === "object" && error !== null && "code" in error
          ? String((error as { code?: unknown }).code)
          : undefined;
      if (
        firebaseCode === "auth/popup-closed-by-user" ||
        firebaseCode === "auth/cancelled-popup-request"
      ) {
        return;
      }
      if (firebaseCode) {
        toast.error("Đăng nhập Google thất bại. Vui lòng thử lại!", {
          position: "bottom-left",
          autoClose: 3000,
        });
        return;
      }
      handleErrorApi({ error });
    }
  };
  return (
    <div className="sm:mx-auto sm:w-full sm:max-w-sm">
      <div className="my-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
        Đăng nhập tài khoản
      </div>
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
            name="phone"
            render={({ field }) => (
              <FormItem>
                <div>
                  <label className="block text-sm/6 font-medium text-gray-900">
                    Số điện thoại
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
            name="password"
            render={({ field }) => (
              <FormItem>
                <div>
                  <div className="flex items-center justify-between">
                    <label className="block text-sm/6 font-medium text-gray-900">
                      Mật khẩu
                    </label>
                    <div className="text-sm">
                      <a
                        href="#"
                        className="font-semibold text-main hover:text-hover"
                      >
                        Quên mật khẩu?
                      </a>
                    </div>
                  </div>
                  <div className="mt-2">
                    <input
                      type="password"
                      className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                      {...field}
                    />
                  </div>
                  <FormMessage />
                </div>
              </FormItem>
            )}
          />

          <div>
            <button
              disabled={loginMutation.isPending}
              className="cursor-pointer flex w-full justify-center rounded-md bg-main px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-main disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Đăng nhập
            </button>
          </div>
        </form>
      </Form>
      <div className="my-5 flex items-center gap-3">
        <div className="h-px flex-1 bg-gray-200" />
        <span className="text-xs uppercase text-gray-400">Hoặc</span>
        <div className="h-px flex-1 bg-gray-200" />
      </div>
      <button
        type="button"
        disabled={googleLoginMutation.isPending}
        onClick={handleGoogleLogin}
        className="cursor-pointer flex w-full items-center justify-center gap-2 rounded-md border border-gray-300 bg-white px-3 py-1.5 text-sm/6 font-semibold text-gray-700 shadow-xs hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        <svg className="h-4 w-4" viewBox="0 0 24 24" aria-hidden="true">
          <path
            fill="#4285F4"
            d="M23.745 12.27c0-.7-.06-1.4-.19-2.07H12v4.51h6.6c-.29 1.52-1.14 2.82-2.4 3.68v3.05h3.88c2.27-2.09 3.665-5.17 3.665-9.17Z"
          />
          <path
            fill="#34A853"
            d="M12 24c3.24 0 5.95-1.08 7.93-2.91l-3.88-3.05c-1.08.72-2.45 1.16-4.05 1.16-3.12 0-5.77-2.11-6.72-4.96H1.29v3.14C3.26 21.3 7.31 24 12 24Z"
          />
          <path
            fill="#FBBC05"
            d="M5.28 14.24c-.24-.72-.38-1.49-.38-2.24s.14-1.52.38-2.24V6.62H1.29C.47 8.24 0 10.06 0 12s.47 3.76 1.29 5.38l3.99-3.14Z"
          />
          <path
            fill="#EA4335"
            d="M12 4.75c1.77 0 3.35.61 4.6 1.8l3.42-3.42C17.95 1.19 15.24 0 12 0 7.31 0 3.26 2.7 1.29 6.62l3.99 3.14c.95-2.85 3.6-4.96 6.72-4.96Z"
          />
        </svg>
        {googleLoginMutation.isPending
          ? "Đang xử lý..."
          : "Đăng nhập bằng Google"}
      </button>
      <p className="mt-10 text-center text-sm/6 text-gray-500">
        Chưa có tài khoản?{" "}
        <span
          className="font-semibold text-main hover:text-hover cursor-pointer"
          onClick={() => {
            props.setActiveTab("register");
          }}
        >
          Đăng ký ngay
        </span>
      </p>
    </div>
  );
}
