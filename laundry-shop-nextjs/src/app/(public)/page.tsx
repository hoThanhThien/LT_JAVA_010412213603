import Comment from "@/components/comment";
import Stores from "@/components/stores";
import Image from "next/image";
import Communication from "@/components/communication";
import Services from "@/components/services";
import { Dropdown } from "antd";
import DropdownAvatar from "./dropdown-avatar";

export default function Home() {
  return (
    <main className="relative">
      <div className="bg-[url('/home/bg-1.png')] bg-no-repeat min-h-[960px] px-4 bg-[length:100%_1060px]">
        <div className="max-w-7xl mx-auto px-4 flex justify-between pt-15">
          <div className="w-[41%] text-left">
            <div className="text-4xl font-bold">
              Hệ thống nhượng quyền & ứng dụng giặt ủi vệ sinh cao cấp 4.0
            </div>
            <div className="text-xl mt-3">
              HERAMO (phát âm "hê-ra-mô") ra đời từ 2017 bởi những chuyên gia
              hàng đầu trong ngành công nghệ & dịch vụ, với tầm nhìn trở thành
              công ty số một trong ngành giặt ủi vệ sinh cao cấp. Tới nay HERAMO
              đã phát triển trở thành thương hiệu dẫn đầu trong ngành, với 10
              chi nhánh trải khắp thành phố Hồ Chí Minh, 136.000+ khách hàng tin
              dùng cùng với hệ thống công nghệ mạnh mẽ. HERAMO cung cấp đầy đủ
              các dịch vụ: vệ sinh spa giày, giặt hấp đồ hiệu, spa túi xách,
              giặt ủi, vệ sinh sofa, nệm, rèm, thảm & máy lạnh. Chúng tôi luôn
              cam kết mang đến trải nghiệm tuyệt vời với chất lượng cao cấp,
              tiện lợi & tuyệt đối an tâm cho quý khách hàng.
            </div>
            <button className="mt-5 text-black hover:text-white w-[70%] text-xl py-3 rounded-[28px] font-medium cursor-pointer border-2 border-black">
              ĐẶT LỊCH DỊCH VỤ NGAY
            </button>
            <button className="mt-5 text-white bg-black hover:bg-main w-[70%] text-xl py-3 rounded-[28px] font-medium cursor-pointer border-2 border-black">
              TẢI ỨNG DỤNG
            </button>
            <div className="flex gap-6 mt-10">
              <Image
                src="/app-store.svg"
                alt="AppStore"
                width="0"
                height="0"
                sizes="100vw"
                className="w-[30%] h-[30%]"
              />
              <Image
                src="/google-play.svg"
                alt="GooglePlay"
                width="0"
                height="0"
                sizes="100vw"
                className="w-[30%] h-[30%]"
              />
            </div>
          </div>
          <div className="w-[50%]">
            <Image
              src="/home/phone-banner-desktop.png"
              alt="Banner"
              width="0"
              height="0"
              sizes="100vw"
              className="w-full h-[552px]"
            />
          </div>
        </div>
      </div>
      <div>
        <Services />
      </div>
      <div className="grid lg:grid-cols-2 max-w-7xl mx-auto">
        <div className="lg:col-span-1 lg:col-start-1">
          <Comment />
        </div>
        <div className="lg:col-span-1 lg:col-start-2">
          <form
            className="bg-white rounded-4xl shadow-2xl py-10 px-20 space-y-10"
            noValidate
          >
            <div className="font-bold text-center text-3xl">ĐẶT DỊCH VỤ</div>
            <div className="text-center mx-auto text-sm w-[60%] block text-gray-400">
              Sau khi quý khách gửi thông tin, chuyên viên từ HERAMO sẽ liên hệ
              trong vòng 15 phút để hỗ trợ tư vấn chi tiết
            </div>
            <div>
              <label className="block text-sm/6 font-medium text-gray-900">
                Cho HERAMO xin tên của bạn nhé *
              </label>
              <div className="mt-2">
                <input
                  type="text"
                  placeholder="Trần Văn A"
                  className="block w-full rounded-md bg-white px-3 py-2.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                />
              </div>
            </div>
            <div>
              <label className="block text-sm/6 font-medium text-gray-900">
                Số điện thoại HERAMO có thể liên hệ là *
              </label>
              <div className="mt-2">
                <input
                  type="text"
                  placeholder="0123456789"
                  className="block w-full rounded-md bg-white px-3 py-2.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                />
              </div>
            </div>
            <div>
              <label className="block text-sm/6 font-medium text-gray-900">
                Bạn cần dịch vụ gì?
              </label>
              <div className="mt-2">
                <input
                  type="text"
                  placeholder="0123456789"
                  className="block w-full rounded-md bg-white px-3 py-2.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-main sm:text-sm/6"
                />
              </div>
            </div>
            <button className="bg-main hover:text-white text-[18px] px-5 py-0 rounded-[28px] h-12 font-medium cursor-pointer mx-auto block">
              ĐẶT DỊCH VỤ NGAY
            </button>
            <div className="text-sm text-center text-gray-400">
              Thông qua việc đặt dịch vụ, bạn đồng ý với các điều khoản sử dụng
              dịch vụ của chúng tôi
            </div>
          </form>
        </div>
      </div>
      <div>
        <Communication />
      </div>
      <div>
        <div className="grid grid-cols-2 bg-[url('/home/phone-tien-trinh.jpg')] bg-center bg-no-repeat bg-cover h-[1124px] px-24 my-12 mt-[-48px]">
          <div className="col-span-1 col-start-1 self-end">
            <Image
              src={"/home/homescreen.png"}
              alt="home-screen"
              width="565"
              height="976"
              // className="w-[40%] h-[290px] mx-auto"
            />
          </div>
          <div className="col-span-1 col-start-2 self-end h-full flex flex-col justify-center items-center gap-15">
            <div className="text-4xl font-bold">
              Tải App Ngay - Ưu Đãi Về Tay!
            </div>
            <div className="text-xl mt-2">
              Đặt tất cả dịch vụ giặt ủi, vệ sinh nhanh chóng. Theo dõi đơn
              hàng, hình ảnh món đồ mọi lúc mọi nơi. Ưu đãi lên đến 100K dành
              tặng khách mới và khách hàng thân thiết
            </div>
            <div className="flex gap-6">
              <Image
                src="/app-store.svg"
                alt="AppStore"
                width="200"
                height="200"
              />
              <Image
                src="/google-play.svg"
                alt="GooglePlay"
                width="200"
                height="200"
              />
            </div>
          </div>
        </div>
      </div>
      <div>
        <Stores />
        <div className="mb-12">
          <div className="text-4xl text-center font-bold">
            Bạn muốn mở một{" "}
            <span className="text-main"> cửa hàng nhượng quyền?</span>
          </div>
          <div className="w-[50%] mx-auto text-center text-xl mt-5 mb-10">
            Hệ thống cửa hàng nhượng quyền giặt ủi, giặt hấp, giặt giày, vệ sinh
            cao cấp với 10 chi nhánh tại Hồ Chí Minh. Tiết kiệm 90% thời gian
            vận hành. Hoàn vốn trong 15-18 tháng. Được cố vấn bởi chuyên gia
            nhượng quyền Nguyễn Phi Vân
          </div>
          <div className="flex justify-center items-center">
            <button className="bg-black text-white px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%] ">
              TÌM HIỂU THÊM
            </button>
          </div>
        </div>
      </div>
      <div className="bg-[url('/home/bg-2.svg')] bg-repeat-round min-h-[800px] px-10">
        <div className="flex justify-center items-center">
          <div className="w-[60%]">
            <Image
              src="/home/quality-banner.png"
              width="733"
              height="725"
              alt="bg-2"
              className="lg:block"
            />
          </div>
          <div className="flex flex-col gap-5 w-[40%]">
            <div className="text-4xl font-bold">Chất lượng</div>
            <div className="text-2xl">
              <div className="font-medium">
                Được tin dùng bởi hơn{" "}
                <span className="text-main">136.000+</span> khách hàng.
              </div>
              <div className="font-normal">
                Máy móc dung dịch hiện đại, chuyên gia giặt ủi vệ sinh lành nghề
                tận tâm
              </div>
            </div>
            <button className="bg-black text-white border border-black hover:bg-transparent hover:text-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[70%] ">
              ĐẶT DỊCH VỤ NGAY
            </button>
          </div>
        </div>
      </div>
      <div className="bg-[url('/home/bg-2.svg')] bg-repeat-round h-fit px-10 ">
        <div className="flex justify-center items-center">
          <div className="w-[50%]">
            <Image
              src="/home/convenient-banner.png"
              width="733"
              height="725"
              alt="bg-2"
              className="lg:block"
            />
          </div>
          <div className="flex flex-col gap-5 w-[50%]">
            <div className="text-4xl font-bold">Tiện lợi</div>
            <div className="text-2xl">
              <div className="font-medium">
                Đặt dịch vụ đa kênh, giao nhận tận nơi
              </div>
              <div className="font-normal">
                Giao nhận từ 09:00 - 21:00 khắp các quận Tp.HCM, hoặc tại hệ
                thống cửa hàng HERAMO
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="bg-[url('/home/bg-3.png')] bg-repeat-round bg-auto px-10 ">
        <div className="flex justify-evenly items-center">
          <div className="flex flex-col gap-5 w-[30%]">
            <div className="text-4xl font-bold">An tâm</div>
            <div className="text-2xl">
              <div className="font-normal">
                Theo dõi đơn hàng mọi lúc mọi nơi. Thảnh thơi tận hưởng cuộc
                sống
              </div>
            </div>
            <button className="bg-black text-white px-9 py-3 rounded-4xl font-bold cursor-pointer w-[70%] ">
              TẢI ỨNG DỤNG
            </button>
          </div>
          <div className="w-[50%] flex justify-end">
            <Image
              src="/home/hassle-free-banner.png"
              width="677"
              height="776"
              alt="bg-2"
              className="lg:block"
            />
          </div>
        </div>
      </div>
    </main>
  );
}
