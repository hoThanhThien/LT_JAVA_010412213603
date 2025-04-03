"use client";

import { Dialog, DialogContent } from "@/components/ui/dialog";
import { DialogTitle } from "@radix-ui/react-dialog";
import Image from "next/image";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { LoginBody, LoginBodyType } from "@/schemaValidations/auth.schema";
import { Form, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { useLoginMutation } from "@/queries/useAuth";
import { handleErrorApi } from "@/lib/utils";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

export default function Login(props: {
  openRegister: boolean;
  setOpenRegister: (open: boolean) => void;
  openLogin: boolean;
  setOpenLogin: (open: boolean) => void;
}) {
  const loginMutation = useLoginMutation();
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
      props.setOpenLogin(false);
      toast.success(result.payload.message, {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
    } catch (error: any) {
      handleErrorApi({ error, setError: form.setError });
    }
  };
  return (
    <>
      <Dialog
        open={props.openLogin}
        onOpenChange={(open: boolean) => props.setOpenLogin(open)}
      >
        <DialogContent className="border-none" aria-describedby={undefined}>
          <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
            <DialogTitle className="sm:mx-auto sm:w-full sm:max-w-sm">
              <Image
                src="/heramo-logo.png"
                alt="Logo"
                width={699}
                height={150}
                className="mx-auto h-10 w-auto"
              />
              <div className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
                Đăng nhập tài khoản
              </div>
            </DialogTitle>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
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
                    <button className="cursor-pointer flex w-full justify-center rounded-md bg-main px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
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
                    props.setOpenLogin(false);
                    props.setOpenRegister(true);
                  }}
                >
                  Đăng ký ngay
                </span>
              </p>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
