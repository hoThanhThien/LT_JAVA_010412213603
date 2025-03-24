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
    <>
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
                Cả nhà mình đều xài app HERAMO để giặt hấp vest cho bố, vệ sinh
                sofa cho mẹ, giặt giày cho chị em mình. Dịch vụ nào cũng đều rất
                chất lượng và nhân viên hỗ trợ nhiệt tình. Strongly recommend!
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
                Lần đầu tiên mình thấy có app để đặt vệ sinh giày, tò mò thử và
                mê luôn. Đặt dịch vụ cực nhanh, phục vụ cực nhiệt tình và chất
                lượng cực khiến mình hài lòng. Mình đã giới thiệu cho bạn cùng
                xài luôn.
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
                Từ trước đến giờ đồ nào cần giặt hấp khô là tôi gọi cho HERAMO,
                giặt rất sạch, rất chuyên nghiệp và nhân viên lịch sự. Nếu bạn
                hay mặc đồ hiệu, chất liệu cao cấp cứ gửi HERAMO là yên tâm nhé!
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
                Ứng dụng HERAMO như gắn liền với chuyện tình của tụi mình, giày
                đôi, đồ đôi, gấu bông hay bất kỳ đồ đạc gì anh ấy tặng mình cũng
                chỉ giao cho HERAMO vệ sinh, rất an tâm. Cảm ơn HERAMO nhé!
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
                Công ty mình có một phòng chuyên dùng để đón khách rất lớn, bao
                nhiêu ghế đệm lưng, thảm… team mình tháng nào cũng đặt app
                HERAMO. Hiệu quả thì miễn bàn, thơm tho sạch sẽ. Rất tiện lợi &
                an tâm.
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
    </>
  );
}
