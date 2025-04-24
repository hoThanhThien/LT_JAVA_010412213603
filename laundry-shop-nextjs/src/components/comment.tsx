"use client";

import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Swiper as SwiperCore } from "swiper/types";
import Image from "next/image";
import { useState } from "react";

export default function Comment() {
  const [swiper, setSwiper] = useState<SwiperCore | null>(null);
  const [activeIndex, setActiveIndex] = useState(0);
  return (
    <div className="grid lg:grid-cols-2 max-w-7xl mx-auto">
      <div className="lg:col-span-1 lg:col-start-1">
        <div className="text-5xl font-bold mt-20 mb-10">
          Khách hàng nói gì về <span className="text-main">HERAMO</span>?
        </div>
        <Swiper
          grabCursor={true}
          onSlideChange={(swiper) => setActiveIndex(swiper.realIndex)}
          onSwiper={setSwiper}
        >
          <SwiperSlide>
            <div className="flex gap-5 text-xl">
              <div>
                <Image src="/quote.svg" alt="quote" width={350} height={350} />
              </div>
              <div className="mt-5">
                <div>
                  Cả nhà mình đều xài app HERAMO để giặt hấp vest cho bố, vệ
                  sinh sofa cho mẹ, giặt giày cho chị em mình. Dịch vụ nào cũng
                  đều rất chất lượng và nhân viên hỗ trợ nhiệt tình. Strongly
                  recommend!
                </div>
                <div className="font-bold mt-5">Anh Đỗ Thuận</div>
                <div className="mt-2">Sinh viên</div>
              </div>
            </div>
          </SwiperSlide>
          <SwiperSlide>
            <div className="flex gap-5 text-xl">
              <div>
                <Image src="/quote.svg" alt="quote" width={350} height={350} />
              </div>
              <div className="mt-5">
                <div>
                  Lần đầu tiên mình thấy có app để đặt vệ sinh giày, tò mò thử
                  và mê luôn. Đặt dịch vụ cực nhanh, phục vụ cực nhiệt tình và
                  chất lượng cực khiến mình hài lòng. Mình đã giới thiệu cho bạn
                  cùng xài luôn.
                </div>
                <div className="font-bold mt-5">Chị Ngọc Châu</div>
                <div className="mt-2">Food Specialist</div>
              </div>
            </div>
          </SwiperSlide>
          <SwiperSlide>
            <div className="flex gap-5 text-xl">
              <div>
                <Image src="/quote.svg" alt="quote" width={350} height={350} />
              </div>
              <div className="mt-5">
                <div>
                  Từ trước đến giờ đồ nào cần giặt hấp khô là tôi gọi cho
                  HERAMO, giặt rất sạch, rất chuyên nghiệp và nhân viên lịch sự.
                  Nếu bạn hay mặc đồ hiệu, chất liệu cao cấp cứ gửi HERAMO là
                  yên tâm nhé!
                </div>
                <div className="font-bold mt-5">Chị Tố Trâm</div>
                <div className="mt-2">Giảng viên</div>
              </div>
            </div>
          </SwiperSlide>
          <SwiperSlide>
            <div className="flex gap-5 text-xl">
              <div>
                <Image src="/quote.svg" alt="quote" width={350} height={350} />
              </div>
              <div className="mt-5">
                <div>
                  Ứng dụng HERAMO như gắn liền với chuyện tình của tụi mình,
                  giày đôi, đồ đôi, gấu bông hay bất kỳ đồ đạc gì anh ấy tặng
                  mình cũng chỉ giao cho HERAMO vệ sinh, rất an tâm. Cảm ơn
                  HERAMO nhé!
                </div>
                <div className="font-bold mt-5">Chị Hà Xuyên</div>
                <div className="mt-2">Chủ shop Online</div>
              </div>
            </div>
          </SwiperSlide>
          <SwiperSlide>
            <div className="flex gap-5 text-xl">
              <div>
                <Image src="/quote.svg" alt="quote" width={350} height={350} />
              </div>
              <div className="mt-5">
                <div>
                  Công ty mình có một phòng chuyên dùng để đón khách rất lớn,
                  bao nhiêu ghế đệm lưng, thảm… team mình tháng nào cũng đặt app
                  HERAMO. Hiệu quả thì miễn bàn, thơm tho sạch sẽ. Rất tiện lợi
                  & an tâm.
                </div>
                <div className="font-bold mt-5">Chị Nguyễn Hồng</div>
                <div className="mt-2">Quản lý</div>
              </div>
            </div>
          </SwiperSlide>
        </Swiper>
        <div className="flex gap-10 mt-10">
          <Image
            src="/home/HP1.jpg"
            alt="HP1"
            width="0"
            height="0"
            sizes="100vw"
            className={`w-[80px] h-[80px] rounded-full transition-opacity ${
              activeIndex !== 0 ? "opacity-40" : "opacity-100"
            }`}
            onClick={() => {
              swiper?.slideTo(0);
            }}
          />
          <Image
            src="/home/HP2.jpg"
            alt="HP2"
            width="0"
            height="0"
            sizes="100vw"
            className={`w-[80px] h-[80px] rounded-full transition-opacity ${
              activeIndex !== 1 ? "opacity-40" : "opacity-100"
            }`}
            onClick={() => {
              swiper?.slideTo(1);
            }}
          />
          <Image
            src="/home/HP3.jpg"
            alt="HP3"
            width="0"
            height="0"
            sizes="100vw"
            className={`w-[80px] h-[80px] rounded-full transition-opacity ${
              activeIndex !== 2 ? "opacity-40" : "opacity-100"
            }`}
            onClick={() => {
              swiper?.slideTo(2);
            }}
          />
          <Image
            src="/home/HP4.jpg"
            alt="HP4"
            width="0"
            height="0"
            sizes="100vw"
            className={`w-[80px] h-[80px] rounded-full transition-opacity ${
              activeIndex !== 3 ? "opacity-40" : "opacity-100"
            }`}
            onClick={() => {
              swiper?.slideTo(3);
            }}
          />
          <Image
            src="/home/HP5.jpg"
            alt="HP5"
            width="0"
            height="0"
            sizes="100vw"
            className={`w-[80px] h-[80px] rounded-full transition-opacity ${
              activeIndex !== 4 ? "opacity-40" : "opacity-100"
            }`}
            onClick={() => {
              swiper?.slideTo(4);
            }}
          />
        </div>
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
          {/* <div>
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
          </div> */}
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
  );
}
