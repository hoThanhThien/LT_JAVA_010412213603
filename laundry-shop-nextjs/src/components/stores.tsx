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
    imgge: "/home/store_1.jpg",
  },
  {
    id: 3,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
  {
    id: 4,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
  {
    id: 5,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
  {
    id: 6,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
  {
    id: 7,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
  {
    id: 8,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
  {
    id: 9,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
  {
    id: 10,
    address: "01 Trường Sơn, Q.Tân Bình",
    imgge: "/home/store_1.jpg",
  },
];

export default function Stores() {
  return (
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
                className="mx-auto mb-5"
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
  );
}
