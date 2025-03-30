"use client";

import { Dialog, DialogContent } from "@/components/ui/dialog";
import { DialogTitle } from "@radix-ui/react-dialog";
import Image from "next/image";

export default function Register(props: {
  openRegister: boolean;
  setOpenRegister: (open: boolean) => void;
  openLogin: boolean;
  setOpenLogin: (open: boolean) => void;
}) {
  return (
    <>
      <Dialog
        open={props.openRegister}
        onOpenChange={(open: boolean) => props.setOpenRegister(open)}
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
                Đăng ký tài khoản
              </div>
            </DialogTitle>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
              <form className="space-y-3" noValidate>
                <div>
                  <label className="block text-sm/6 font-medium text-gray-900">
                    Họ và tên
                  </label>
                  <div className="mt-2">
                    <input
                      type="text"
                      className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                    />
                  </div>
                  <div className="mt-1 text-red-600 min-h-[1.25rem] text-sm">
                    Báo lỗi
                  </div>
                </div>

                <div>
                  <label className="block text-sm/6 font-medium text-gray-900">
                    Số điện thoại
                  </label>
                  <div className="mt-2">
                    <input
                      type="text"
                      className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                    />
                  </div>
                  <div className="mt-1 text-red-600 min-h-[1.25rem] text-sm">
                    Báo lỗi
                  </div>
                </div>

                <div>
                  <label className="block text-sm/6 font-medium text-gray-900">
                    Mật khẩu
                  </label>
                  <div className="mt-2">
                    <input
                      type="password"
                      className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                    />
                  </div>
                  <div className="mt-1 text-red-600 min-h-[1.25rem] text-sm">
                    Báo lỗi
                  </div>
                </div>

                <div>
                  <button className="flex w-full justify-center rounded-md bg-main px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                    Đăng ký
                  </button>
                </div>
              </form>

              <p className="mt-10 text-center text-sm/6 text-gray-500">
                Đã có tài khoản?{" "}
                <span
                  className="font-semibold text-main hover:text-hover cursor-pointer"
                  onClick={() => {
                    props.setOpenRegister(false);
                    props.setOpenLogin(true);
                  }}
                >
                  Đăng nhập
                </span>
              </p>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
