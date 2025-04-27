"use client";

import Link from "next/link";
import Image from "next/image";
import { useEffect } from "react";
import { getAccessTokenFromLocalStorage } from "@/lib/utils";
import Auth from "./auth";
import DropdownAvatar from "./dropdown-avatar";
import { useAuthStore } from "@/lib/zustand";
import Setting from "./setting";
import Order from "./order";
import DropdownService from "./dropdown-services";
import CustomerListOrder from "./customerOrder";

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const { isAuth, setIsAuth, setOpenAuth } = useAuthStore();

  useEffect(() => {
    setIsAuth(Boolean(getAccessTokenFromLocalStorage()));
  }, [setIsAuth]);

  return (
    <div className="flex min-h-screen w-full flex-col relative">
      <header className="fixed top-0 right-0 z-50 w-full md:h-24 bg-black flex items-center">
        <div className="p-0 mx-auto">
          <nav className="flex py-0 px-11 gap-20">
            <Link href="/">
              <Image
                src="/heramo-logo.png"
                alt="Logo"
                width="0"
                height="0"
                sizes="100vw"
                className="w-auto h-[52px]"
              />
            </Link>
            <div className="flex items-center gap-12 text-lg text-white">
              <Link href="/ve-chung-toi" className="hover:text-main">
                Về chúng tôi
              </Link>

              <Link href="/nhuong-quyen" className="hover:text-main">
                Nhượng quyền
              </Link>

              <DropdownService />

              <Link href="/tuyen-dung" className="hover:text-main">
                Tuyển dụng
              </Link>

              <Link href="/doi-tac" className="hover:text-main">
                Trở thành đối tác
              </Link>
            </div>
            <div>
              {!isAuth ? (
                <button
                  className="bg-main hover:text-white text-[18px] px-10 py-0 rounded-[28px] h-12 font-medium cursor-pointer"
                  onClick={() => setOpenAuth(true)}
                >
                  Đăng nhập
                </button>
              ) : (
                <div className="flex items-center justify-center h-full">
                  <DropdownAvatar />
                </div>
              )}
            </div>
          </nav>
        </div>
        <Auth />
        <Setting />
        <CustomerListOrder />
        <Order />
      </header>
      <main className="flex flex-1 flex-col mt-24 gap-4 md:gap-8">
        {children}
      </main>
      <footer className="bg-black w-full py-12 text-xl text-gray-400 leading-loose">
        <div className="w-full mx-auto lg:max-w-6xl">
          <div className="text-white">Liên hệ</div>
          <span>Hotline : </span>
          <span className="text-white">19003286</span>
          <div>HERAMO JSC | GPKD /MST: 0314624471</div>
          <div>
            Văn phòng đại diện: 01 Trường Sơn, Phường 4, Quận Tân Bình, HCM
          </div>
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
          <div className="flex flex-col gap-4">
            <div>
              <Image
                src="/heramo-logo.png"
                alt="Logo"
                width="0"
                height="0"
                sizes="100vw"
                className="w-auto h-[50px]"
              />
            </div>
            <div className="text-white">
              HERAMO - Hệ thống nhượng quyền & Ứng dụng giặt ủi vệ sinh cao cấp
              4.0
            </div>
            <div className="text-white text-base">
              HERAMO bắt đầu hành trình từ năm 2017 - với khát khao xây dựng một
              ứng dụng giúp hàng triệu người có thể đặt các dịch vụ giặt ủi ,
              giặt hấp , vệ sinh giày , vệ sinh nhà cửa , vệ sinh máy lạnh tiện
              lợi , từ đó, mọi người sẽ có thêm nhiều thời gian để tận hưởng
              cuộc sống.
            </div>
            <div className="text-white text-base">
              Sau hơn 6 năm hoạt động và tiên phong ứng dụng công nghệ 4.0,
              HERAMO tự hào là thương hiệu dẫn đầu trong ngành giặt là, giặt
              hấp, vệ sinh chăm sóc giày, vệ sinh sofa, nệm, rèm, thảm, vệ sinh
              máy lạnh tại TP.Hồ Chí Minh với 136.000+ khách hàng tin dùng.
            </div>
            <div className="text-white text-base">
              Tại HERAMO, khách hàng có thể đặt tất cả dịch vụ giặt ủi, vệ sinh
              chỉ với một chạm duy nhất:
            </div>
          </div>
          <div className="grid lg:grid-cols-4 lg:mt-12">
            <div className="lg:col-span-1 lg:col-start-1">
              <div className="text-white">Công ty</div>
              <div>Về chúng tôi</div>
              <div>Nhượng quyền thương hiệu</div>
              <div>Tuyển dụng</div>
              <div>Trở thành đối tác</div>
              <div>Blog</div>
              <div>Điều khoản sử dụng</div>
            </div>
            <div className="lg:col-span-1 lg:col-start-2">
              <div className="text-white">Dịch vụ</div>
              <div>Giặt sấy</div>
              <div>Giặt hấp</div>
              <div>Vệ sinh giày</div>
              <div>Vệ sinh sofa, nệm, rèm, thảm</div>
              <div>Vệ sinh máy lạnh</div>
            </div>
          </div>
          <hr className="border-t border-gray-400 my-10" />
          <div className="grid lg:grid-cols-2">
            <div className="lg:col-span-1 lg:col-start-1 flex gap-8">
              <Link href={"/"}>
                <Image
                  src="/footer/fb-icon.svg"
                  alt="fb"
                  width={0}
                  height={0}
                  className="w-auto h-[50px]"
                />
              </Link>
              <Link href={"/"}>
                <Image
                  src="/footer/zalo-icon.svg"
                  alt="zalo"
                  width={0}
                  height={0}
                  className="w-auto h-[50px]"
                />
              </Link>
              <Link href={"/"}>
                <Image
                  src="/footer/ins-icon.svg"
                  alt="inx"
                  width={0}
                  height={0}
                  className="w-auto h-[50px]"
                />
              </Link>
              <Link href={"/"}>
                <Image
                  src="/footer/youtube-icon.svg"
                  alt="youtube"
                  width={0}
                  height={0}
                  className="w-auto h-[50px]"
                />
              </Link>
              <Link href={"/"}>
                <Image
                  src="/footer/linkedin-icon.svg"
                  alt="linkedin"
                  width={0}
                  height={0}
                  className="w-auto h-[50px]"
                />
              </Link>
            </div>
            <div className="lg:col-span-1 lg:col-start-2 text-right text-sm">
              Copyrights 2023 @ HERAMO.All rights reserved
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}
