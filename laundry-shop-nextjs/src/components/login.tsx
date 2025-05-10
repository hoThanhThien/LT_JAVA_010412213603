"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { LoginBody, LoginBodyType } from "@/schemaValidations/auth.schema";
import { Form, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { useLoginMutation } from "@/queries/useAuth";
import { handleErrorApi } from "@/lib/utils";
import { toast } from "react-toastify";
import { useAuthStore } from "@/lib/zustand";
import { useRouter, useSearchParams } from "next/navigation";

export default function Login(props: {
  openAuth: boolean;
  setOpenAuth: (open: boolean) => void;
  setActiveTab: (tab: string) => void;
}) {
  const loginMutation = useLoginMutation();
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
