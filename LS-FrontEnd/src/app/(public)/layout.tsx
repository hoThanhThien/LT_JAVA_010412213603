"use client";

import Link from "next/link";
import Image from "next/image";
import dynamic from "next/dynamic";
import { Suspense, useEffect, useState } from "react";
import { Menu, X } from "lucide-react";
import { getSession } from "@/lib/utils";
import Auth from "./auth";
import DropdownAvatar from "./dropdown-avatar";
import { useAuthStore } from "@/lib/zustand";
import DropdownService from "./dropdown-services";
import ButtonOrder from "@/components/ui/buttonOrder";

const Setting = dynamic(() => import("./setting"), { ssr: false });
const Order = dynamic(() => import("./order"), { ssr: false });
const CustomerListOrder = dynamic(() => import("./customerOrder"), {
  ssr: false,
});

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const { isAuth, setIsAuth, setOpenAuth } = useAuthStore();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  useEffect(() => {
    getSession()
      .then((session) => setIsAuth(session.authenticated))
      .catch(() => setIsAuth(false));
  }, [setIsAuth]);

  return (
    <div className="flex min-h-screen w-full flex-col relative">
      <header className="fixed top-0 left-0 right-0 z-50 w-full bg-black/95 backdrop-blur-xs border-b border-white/10">
        <div className="mx-auto w-full max-w-7xl px-4 sm:px-6 lg:px-8 h-16 md:h-20 flex items-center justify-between gap-3">
          <Link
            href="/"
            className="shrink-0"
            onClick={() => setMobileMenuOpen(false)}
          >
            <Image
              src="/heramo-logo.png"
              alt="Logo"
              width="0"
              height="0"
              sizes="100vw"
              className="w-auto h-9 sm:h-11 md:h-[50px]"
            />
          </Link>
          <nav className="hidden lg:flex items-center gap-6 xl:gap-10 text-[15px] xl:text-base font-medium text-white">
            <Link
              href="/ve-chung-toi"
              className="hover:text-main whitespace-nowrap transition-colors"
            >
              Về chúng tôi
            </Link>

            <Link
              href="/nhuong-quyen"
              className="hover:text-main whitespace-nowrap transition-colors"
            >
              Nhượng quyền
            </Link>

            <DropdownService />

            <Link
              href="/tuyen-dung"
              className="hover:text-main whitespace-nowrap transition-colors"
            >
              Tuyển dụng
            </Link>

            <Link
              href="/doi-tac"
              className="hover:text-main whitespace-nowrap transition-colors"
            >
              Trở thành đối tác
            </Link>
          </nav>
          <div className="flex items-center gap-2 sm:gap-3 shrink-0">
            <ButtonOrder>
              <button
                type="button"
                className="bg-main hover:bg-hover text-black text-xs sm:text-sm md:text-[15px] px-3.5 sm:px-5 md:px-6 py-2 rounded-[28px] h-9 sm:h-10 md:h-11 font-bold cursor-pointer whitespace-nowrap transition-colors"
              >
                Đặt dịch vụ
              </button>
            </ButtonOrder>
            {!isAuth ? (
              <button
                type="button"
                className="hidden sm:inline-flex items-center justify-center border border-main text-main hover:bg-main hover:text-black text-xs sm:text-sm md:text-[15px] px-4 md:px-6 py-2 rounded-[28px] h-9 sm:h-10 md:h-11 font-semibold cursor-pointer whitespace-nowrap transition-colors"
                onClick={() => setOpenAuth(true)}
              >
                Đăng nhập
              </button>
            ) : (
              <div className="flex items-center justify-center">
                <DropdownAvatar />
              </div>
            )}
            <button
              type="button"
              onClick={() => setMobileMenuOpen((prev) => !prev)}
              aria-label={mobileMenuOpen ? "Đóng menu" : "Mở menu"}
              aria-expanded={mobileMenuOpen}
              className="lg:hidden inline-flex h-10 w-10 cursor-pointer items-center justify-center rounded-xl border border-white/15 bg-zinc-900 text-white hover:border-main hover:text-main transition-colors"
            >
              {mobileMenuOpen ? (
                <X className="h-5 w-5" />
              ) : (
                <Menu className="h-5 w-5" />
              )}
            </button>
          </div>
        </div>

        {mobileMenuOpen && (
          <div className="lg:hidden border-t border-white/10 bg-zinc-950/98 px-4 py-4 text-white shadow-xl max-h-[calc(100dvh-4rem)] overflow-y-auto">
            <nav className="flex flex-col gap-1 text-base">
              <Link
                href="/ve-chung-toi"
                onClick={() => setMobileMenuOpen(false)}
                className="rounded-lg px-3 py-2.5 hover:bg-white/10 hover:text-main transition-colors"
              >
                Về chúng tôi
              </Link>
              <Link
                href="/nhuong-quyen"
                onClick={() => setMobileMenuOpen(false)}
                className="rounded-lg px-3 py-2.5 hover:bg-white/10 hover:text-main transition-colors"
              >
                Nhượng quyền
              </Link>
              <div className="px-3 pt-2 pb-1 text-xs font-bold uppercase tracking-wider text-main">
                Dịch vụ
              </div>
              <div className="flex flex-col pl-3 gap-0.5 text-sm text-gray-200">
                <Link
                  href="/dich-vu/giat-hap-giat-kho"
                  onClick={() => setMobileMenuOpen(false)}
                  className="rounded-lg px-3 py-2 hover:bg-white/10 hover:text-main transition-colors"
                >
                  Giặt hấp, giặt khô
                </Link>
                <Link
                  href="/dich-vu/giat-say"
                  onClick={() => setMobileMenuOpen(false)}
                  className="rounded-lg px-3 py-2 hover:bg-white/10 hover:text-main transition-colors"
                >
                  Giặt sấy
                </Link>
                <Link
                  href="/dich-vu/ve-sinh-giay"
                  onClick={() => setMobileMenuOpen(false)}
                  className="rounded-lg px-3 py-2 hover:bg-white/10 hover:text-main transition-colors"
                >
                  Vệ sinh giày
                </Link>
                <Link
                  href="/dich-vu/ve-sinh-giat-sofa-nem-rem-tham"
                  onClick={() => setMobileMenuOpen(false)}
                  className="rounded-lg px-3 py-2 hover:bg-white/10 hover:text-main transition-colors"
                >
                  Vệ sinh sofa, nệm, rèm, thảm
                </Link>
                <Link
                  href="/dich-vu/ve-sinh-may-lanh"
                  onClick={() => setMobileMenuOpen(false)}
                  className="rounded-lg px-3 py-2 hover:bg-white/10 hover:text-main transition-colors"
                >
                  Vệ sinh máy lạnh
                </Link>
              </div>
              <Link
                href="/tuyen-dung"
                onClick={() => setMobileMenuOpen(false)}
                className="rounded-lg px-3 py-2.5 hover:bg-white/10 hover:text-main transition-colors"
              >
                Tuyển dụng
              </Link>
              <Link
                href="/doi-tac"
                onClick={() => setMobileMenuOpen(false)}
                className="rounded-lg px-3 py-2.5 hover:bg-white/10 hover:text-main transition-colors"
              >
                Trở thành đối tác
              </Link>
              <div className="mt-3 pt-3 border-t border-white/10 flex flex-col gap-2">
                <ButtonOrder className="w-full">
                  <button
                    type="button"
                    onClick={() => setMobileMenuOpen(false)}
                    className="w-full bg-main hover:bg-hover text-black py-2.5 px-4 rounded-[28px] font-bold cursor-pointer transition-colors"
                  >
                    Đặt dịch vụ ngay
                  </button>
                </ButtonOrder>
                {!isAuth && (
                  <button
                    type="button"
                    onClick={() => {
                      setMobileMenuOpen(false);
                      setOpenAuth(true);
                    }}
                    className="w-full border border-main text-main hover:bg-main hover:text-black py-2.5 px-4 rounded-[28px] font-semibold cursor-pointer transition-colors"
                  >
                    Đăng nhập / Đăng ký
                  </button>
                )}
              </div>
            </nav>
          </div>
        )}

        <Suspense fallback={null}>
          <Auth />
        </Suspense>
        {isAuth && <Setting />}
        {isAuth && <CustomerListOrder />}
        {isAuth && <Order />}
      </header>
      <main className="flex flex-1 flex-col mt-16 md:mt-20 gap-4 md:gap-8">
        {children}
      </main>
      <footer className="bg-black w-full py-10 px-4 sm:px-6 lg:px-8 text-sm sm:text-base text-gray-400 leading-relaxed">
        <div className="w-full mx-auto max-w-7xl space-y-6">
          <div className="space-y-1.5">
            <div className="text-white font-semibold text-base sm:text-lg">
              Liên hệ
            </div>
            <div>
              <span>Hotline : </span>
              <span className="text-white font-semibold">19003286</span>
            </div>
            <div>HERAMO JSC | GPKD /MST: 0314624471</div>
            <div>
              Văn phòng đại diện: 01 Trường Sơn, Phường 4, Quận Tân Bình, HCM
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-1 pt-2 text-xs sm:text-sm">
              <div>CN1: 01 Trường Sơn, Phường 4, Quận Tân Bình, HCM</div>
              <div>
                CN2: 18A/71 Nguyễn Thị Minh Khai, Phường Đa Kao, Quận 1, HCM
              </div>
              <div>CN3: 13C1 Hồ Hảo Hớn, Phường Cô Giang, Quận 1, HCM</div>
              <div>CN4: 197 Nguyễn Văn Lượng, Phường 10, Quận Gò Vấp, HCM</div>
              <div>CN5: 85 Võ Thị Sáu, Phường 6, Quận 3</div>
              <div>CN6: 179B, Đường 3/2, Phường 11, Quận 10</div>
              <div>CN7: 314 Lê Văn Việt, Phường Tân Nhơn Phú B, Quận 9</div>
              <div>CN8: 15A Đường Số 2, Phường An Khánh, TP.Thủ Đức</div>
              <div>CN9: 398 Hoàng Diệu, Phường 2, Quận 4, HCM</div>
              <div>
                CN10: PARC VILLA Đảo Đại Phước (Swanbay), Nhơn Trạch, Đồng Nai
              </div>
            </div>
          </div>
          <div className="flex flex-col gap-3">
            <div>
              <Image
                src="/heramo-logo.png"
                alt="Logo"
                width="0"
                height="0"
                sizes="100vw"
                className="w-auto h-[44px]"
              />
            </div>
            <div className="text-white font-semibold">
              HERAMO - Hệ thống nhượng quyền & Ứng dụng giặt ủi vệ sinh cao cấp
              4.0
            </div>
            <div className="text-gray-300 text-sm sm:text-base">
              HERAMO bắt đầu hành trình từ năm 2017 - với khát khao xây dựng một
              ứng dụng giúp hàng triệu người có thể đặt các dịch vụ giặt ủi ,
              giặt hấp , vệ sinh giày , vệ sinh nhà cửa , vệ sinh máy lạnh tiện
              lợi , từ đó, mọi người sẽ có thêm nhiều thời gian để tận hưởng
              cuộc sống.
            </div>
            <div className="text-gray-300 text-sm sm:text-base">
              Sau hơn 6 năm hoạt động và tiên phong ứng dụng công nghệ 4.0,
              HERAMO tự hào là thương hiệu dẫn đầu trong ngành giặt là, giặt
              hấp, vệ sinh chăm sóc giày, vệ sinh sofa, nệm, rèm, thảm, vệ sinh
              máy lạnh tại TP.Hồ Chí Minh với 136.000+ khách hàng tin dùng.
            </div>
            <div className="text-gray-300 text-sm sm:text-base">
              Tại HERAMO, khách hàng có thể đặt tất cả dịch vụ giặt ủi, vệ sinh
              chỉ với một chạm duy nhất:
            </div>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 pt-2">
            <div>
              <div className="text-white font-semibold mb-2">Công ty</div>
              <div className="space-y-1 text-sm">
                <div>Về chúng tôi</div>
                <div>Nhượng quyền thương hiệu</div>
                <div>Tuyển dụng</div>
                <div>Trở thành đối tác</div>
                <div>Blog</div>
                <div>Điều khoản sử dụng</div>
              </div>
            </div>
            <div>
              <div className="text-white font-semibold mb-2">Dịch vụ</div>
              <div className="space-y-1 text-sm">
                <div>Giặt sấy</div>
                <div>Giặt hấp</div>
                <div>Vệ sinh giày</div>
                <div>Vệ sinh sofa, nệm, rèm, thảm</div>
                <div>Vệ sinh máy lạnh</div>
              </div>
            </div>
          </div>
          <hr className="border-t border-gray-700 my-6" />
          <div className="flex flex-col sm:flex-row items-center justify-between gap-4">
            <div className="flex flex-wrap justify-center gap-4 sm:gap-6">
              <Link href={"/"}>
                <Image
                  src="/footer/fb-icon.svg"
                  alt="fb"
                  width={0}
                  height={0}
                  className="w-auto h-[40px]"
                />
              </Link>
              <Link href={"/"}>
                <Image
                  src="/footer/zalo-icon.svg"
                  alt="zalo"
                  width={0}
                  height={0}
                  className="w-auto h-[40px]"
                />
              </Link>
              <Link href={"/"}>
                <Image
                  src="/footer/ins-icon.svg"
                  alt="inx"
                  width={0}
                  height={0}
                  className="w-auto h-[40px]"
                />
              </Link>
              <Link href={"/"}>
                <Image
                  src="/footer/youtube-icon.svg"
                  alt="youtube"
                  width={0}
                  height={0}
                  className="w-auto h-[40px]"
                />
              </Link>
              <Link href={"/"}>
                <Image
                  src="/footer/linkedin-icon.svg"
                  alt="linkedin"
                  width={0}
                  height={0}
                  className="w-auto h-[40px]"
                />
              </Link>
            </div>
            <div className="text-center sm:text-right text-sm">
              Copyrights 2023 @ HERAMO.All rights reserved
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}
