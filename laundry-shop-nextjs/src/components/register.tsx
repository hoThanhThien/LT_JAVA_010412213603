"use client";

import {
  RegisterBody,
  RegisterBodyType,
} from "@/schemaValidations/auth.schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { Form, FormField, FormItem, FormMessage } from "@/components/ui/form";

export default function Register(props: {
  openAuth: boolean;
  setOpenAuth: (open: boolean) => void;
  setActiveTab: (tab: string) => void;
  setDataRegister: (data: RegisterBodyType) => void;
}) {
  const form = useForm<RegisterBodyType>({
    resolver: zodResolver(RegisterBody),
    defaultValues: {
      username: "",
      phone: "",
      password: "",
    },
  });

  const onSubmit = async (data: RegisterBodyType) => {
    props.setDataRegister(data);
    setTimeout(() => {
      props.setActiveTab("otp");
    }, 100);
  };

  return (
    <div className="sm:mx-auto sm:w-full sm:max-w-sm">
      <div className="my-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
        Đăng ký tài khoản
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
            name="username"
            render={({ field }) => (
              <FormItem>
                <div>
                  <label className="block text-sm/6 font-medium text-gray-900">
                    Họ và tên
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
            name="password"
            render={({ field }) => (
              <FormItem>
                <div>
                  <label className="block text-sm/6 font-medium text-gray-900">
                    Mật khẩu
                  </label>
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
            <button className="cursor-pointer flex w-full justify-center rounded-md bg-main px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
              Đăng ký
            </button>
          </div>
        </form>
      </Form>
      <p className="mt-10 text-center text-sm/6 text-gray-500">
        Đã có tài khoản?{" "}
        <span
          className="font-semibold text-main hover:text-hover cursor-pointer"
          onClick={() => {
            props.setActiveTab("login");
          }}
        >
          Đăng nhập
        </span>
      </p>
    </div>
  );
}
