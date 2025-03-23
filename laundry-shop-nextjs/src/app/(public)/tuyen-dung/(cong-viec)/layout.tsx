import Image from "next/image";
import Link from "next/link";

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="relative w-full">
      <header className="py-5">
        <Image
          src="/heramo-logo.png"
          alt="Logo"
          width="233"
          height="50"
          className="mx-auto"
        />
      </header>
      <main className="flex flex-1 flex-col gap-4 md:gap-8">{children}</main>
      <footer>
        <Link href="/tuyen-dung">
          <div className="text-main text-center py-5 cursor-pointer">
            {"<<"} Quay lại trang công việc
          </div>
        </Link>
        <div className="bg-main flex flex-col justify-center items-center gap-5 py-12">
          <div className="text-center w-[30%]">
            Bạn còn chờ đợi gì nữa? Nhấn nút ứng tuyển để nộp hồ sơ hoặc liên hệ
            HERAMO để biết thêm chi tiết nhé.
          </div>
          <button className="bg-black text-white px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%]">
            ỨNG TUYỂN NGAY
          </button>
        </div>
      </footer>
    </div>
  );
}
