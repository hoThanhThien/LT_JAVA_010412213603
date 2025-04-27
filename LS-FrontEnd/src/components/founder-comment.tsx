"use client";

import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Swiper as SwiperCore } from "swiper/types";
import Image from "next/image";
import { useState } from "react";

export default function FounderComment() {
  const [swiper, setSwiper] = useState<SwiperCore | null>(null);
  const [activeIndex, setActiveIndex] = useState(0);
  return (
    <>
      <div className="text-5xl font-bold mt-20 mb-10">
        Chia sẻ từ chuyên gia nhượng quyền
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
                Nằm uống cocktail ngoài biển cũng quản trị được chi nhánh: Nền
                tảng quản trị của Heramo có đầy đủ app cho nhân viên, app cho
                chi nhánh (shop app), app cho khách hàng. Chỉ cần xài 10% thời
                gian & công sức quản trị so với mô hình truyền thống. Mô hình
                nhượng quyền công nghệ có mức đầu tư và hoàn vốn xịn sò nhất
                Việt Nam & khu vực. Đây thật sự là mô hình của tương lai.
              </div>
              <div className="font-bold mt-5">Bà Nguyễn Phi Vân</div>
              <div className="mt-2">
                Chuyên gia nhượng quyền số 1 tại Việt Nam
              </div>
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
                Heramo không ngừng xây dựng, cải tiến hệ thống công nghệ, quy
                trình dịch vụ. Quan trọng nhất là việc ứng dụng công nghệ giúp
                theo dõi mọi thao tác trong quy trình vận hành, giảm thiểu tối
                đa rủi ro và kiểm soát chặt chẽ chất lượng xử lý - một vấn đề
                sống còn trong ngành giặt ủi, vệ sinh
              </div>
              <div className="font-bold mt-5">Ông Lê Phước Phúc</div>
              <div className="mt-2">
                CEO & Co-founder chuỗi hệ thống 10 cửa hàng Heramo
              </div>
            </div>
          </div>
        </SwiperSlide>
      </Swiper>
      <div className="flex gap-10 mt-10">
        <Image
          src="/nhuong-quyen/founder_2.png"
          alt="founder_1"
          width="80"
          height="80"
          className={`rounded-full transition-opacity ${
            activeIndex !== 0 ? "opacity-40" : "opacity-100"
          }`}
          onClick={() => {
            swiper?.slideTo(0);
          }}
        />
        <Image
          src="/nhuong-quyen/founder_1.png"
          alt="founder_2"
          width="80"
          height="80"
          className={`rounded-full transition-opacity ${
            activeIndex !== 1 ? "opacity-40" : "opacity-100"
          }`}
          onClick={() => {
            swiper?.slideTo(1);
          }}
        />
      </div>
    </>
  );
}
