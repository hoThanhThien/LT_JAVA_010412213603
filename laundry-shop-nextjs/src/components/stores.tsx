"use client";

import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Navigation } from "swiper/modules";
import Image from "next/image";

const listStores = [
  {
    id: 1,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
  {
    id: 2,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_2.jpg",
  },
  {
    id: 3,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_3.jpg",
  },
  {
    id: 4,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_4.jpg",
  },
  {
    id: 5,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_5.jpg",
  },
  {
    id: 6,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_6.png",
  },
  {
    id: 7,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_7.jpg",
  },
  {
    id: 8,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_8.jpg",
  },
  {
    id: 9,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_9.jpg",
  },
  {
    id: 10,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_10.jpg",
  },
];

export default function Stores() {
  return (
    <div className="max-w-7xl mx-auto px-4 py-12">
      <div className="text-4xl text-center font-bold">
        Hệ thống 10 cửa hàng tại HCM của HERAMO
      </div>
      <div className="mt-20">
        <Swiper
          grabCursor={true}
          slidesPerView={4}
          navigation={true}
          spaceBetween={20}
          pagination={true}
          modules={[Navigation]}
        >
          {listStores.map((item) => {
            return (
              <SwiperSlide key={item.id}>
                <div className="rounded-4xl border border-gray-300 w-fit p-5">
                  <Image
                    src={item.imgge}
                    alt={item.address}
                    width="234"
                    height="187"
                    className="mx-auto mb-5 w-[234px] h-[187px] rounded-4xl"
                  />
                  <div className="flex items-center gap-2 max-w-[234px]">
                    <span className="text-xl font-bold bg-main rounded-full aspect-square w-10 h-10 flex items-center justify-center">
                      {item.id}
                    </span>
                    <div className="text-xl font-bold">{item.address}</div>
                  </div>
                </div>
              </SwiperSlide>
            );
          })}
        </Swiper>
      </div>
    </div>
  );
}
