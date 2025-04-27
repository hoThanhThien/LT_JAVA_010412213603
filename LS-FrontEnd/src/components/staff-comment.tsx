"use client";

import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Swiper as SwiperCore } from "swiper/types";
import Image from "next/image";
import { useState } from "react";

const listStaffComment = [
  {
    id: 0,
    name: "Thu Thanh",
    position: "Organization Development Team",
    image: "/tuyen-dung/Thanh_Huỳnh_OD.jpg",
    comment:
      "Điều mình thích ở startup HERAMO trước khi quyết định gắn bó đó chính là những giá trị cốt lõi và văn hóa customer centric (luôn lấy khách hàng làm trọng tâm). Khi công ty đề cao văn hóa và khách hàng, mình biết tham gia bộ phận OD sẽ có rất nhiều đất để dụng võ và đặc biệt là phát triển con người. Gắn bó ở công ty đã nửa năm, mình biết rằng mình đã lựa chọn đúng.",
  },
  {
    id: 1,
    name: "Khánh Linh",
    position: "Marketing Team",
    image: "/tuyen-dung/khanh_linh.jpg",
    comment:
      "HERAMO là môi trường làm việc trong các cuốn sách khởi nghiệp, “Start with why” của Simon Sinek thường hay nhắc đến. Ở HERAMO, bạn được các sếp truyền cảm hứng, feedback máu lửa từ tập thể, tự do phát huy mọi sở trường, dấn thân làm mọi điều bạn muốn miễn là ra việc và mang lại cho khách hàng điều tuyệt vời nhất.",
  },
  {
    id: 2,
    name: "Quách Bảo",
    position: "Project Coordinator",
    image: "/tuyen-dung/Gia_Bảo.jpg",
    comment:
      "Trước mình muốn thực tập ở môi trường sang chảnh, fancy các thứ. Vào HERAMO… dù không có những điều trên nhưng mình mới thấy đây là quyết định đúng nhứt cuộc đời. Là sinh viên Ngôn ngữ Anh mà vào đây mình lăn lộn đủ thứ, từ giám sát dự án, xây dựng cửa hàng, đề xuất chiến lược. Toàn các anh chị em 9x và gen Z nên ngoài lúc làm việc căng thì mọi người cũng “quậy” cực dữ.",
  },
  {
    id: 3,
    name: "Khánh Vy",
    position: "Customer Excellent Team",
    image: "/tuyen-dung/Vy_LÊ.jpg",
    comment:
      "Mình bắt đầu ở vị trí part-time, lúc đầu chỉ là dự định tạm thời sau tốt nghiệp. Nhưng chị quản lý và đồng nghiệp ở HERAMO khiến mình bị thuyết phục 100% và muốn gắn bó, trở thành 1 phần của đại gia đình. Ở đây, mình nói nhiều hơn về lợi ích mang lại cho khách hàng, phúc lợi của nhân viên thay vì chỉ chăm chăm vào chốt sales. Mình thật sự được truyền cảm hứng mỗi ngày.",
  },
  {
    id: 4,
    name: "Châu Giang",
    position: "Intern",
    image: "/tuyen-dung/Châu_Giang.jpg",
    comment:
      "Năng động, trẻ trung, tích cực và quan trọng hơn hết, HERAMO luôn tạo điều kiện để nhân viên phát triển bản thân (điều mà một sinh viên mới bước chân vào ngành như mình rất cần). Mọi người luôn sẵn sàng hỗ trợ lẫn nhau. HERAMO là môi trường lý tưởng để những người trẻ như mình phát triển và khám phá bản thân.",
  },
];

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
        {listStaffComment.map((item) => {
          return (
            <SwiperSlide key={item.id}>
              <div className="flex gap-5 text-xl">
                <div>
                  <Image
                    src="/quote.svg"
                    alt="quote"
                    width={350}
                    height={350}
                  />
                </div>
                <div className="mt-5">
                  <div>{item.comment}</div>
                  <div className="font-bold mt-5">{item.name}</div>
                  <div className="mt-2">{item.position}</div>
                </div>
              </div>
            </SwiperSlide>
          );
        })}
      </Swiper>
      <div className="flex gap-10 mt-10">
        {listStaffComment.map((item) => {
          return (
            <Image
              key={item.id}
              src={item.image}
              alt={item.name}
              width="80"
              height="80"
              className={`w-[80px] h-[80px] rounded-full transition-opacity ${
                activeIndex !== item.id ? "opacity-40" : "opacity-100"
              }`}
              onClick={() => {
                swiper?.slideTo(item.id);
              }}
            />
          );
        })}
      </div>
    </>
  );
}
