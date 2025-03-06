import { Link } from "react-router";
import Logo from "@/assets/heramo-logo.png";

export default function Header() {
  return (
    <header className="w-full md:h-24 bg-black flex items-center">
      <div className="p-0 mx-auto">
        <nav className="flex py-0 px-11 gap-20">
          <Link to={"/"}>
            <img className="w-auto h-[52px]" src={Logo} alt="Logo" />
          </Link>
          <div className="flex items-center gap-12 text-lg text-white">
            <Link to={"/"} className="hover:text-main">
              Về chúng tôi
            </Link>

            <Link to={"/"} className="hover:text-main">
              Nhượng quyền
            </Link>

            <Link to={"/"} className="hover:text-main">
              Dịch vụ
            </Link>

            <Link to={"/"} className="hover:text-main">
              Tuyển dụng
            </Link>

            <Link to={"/"} className="hover:text-main">
              Trở thành đối tác
            </Link>
          </div>
          <div className="flex gap-5">
            <button className="bg-main text-[18px] px-10 py-0 rounded-[28px] h-12 font-medium cursor-pointer">
              Đăng nhập
            </button>
            <button className="text-white text-[18px] px-10 py-0 rounded-[28px] h-12 font-medium cursor-pointer border border-main">
              Đăng ký
            </button>
          </div>
        </nav>
      </div>
    </header>
  );
}
