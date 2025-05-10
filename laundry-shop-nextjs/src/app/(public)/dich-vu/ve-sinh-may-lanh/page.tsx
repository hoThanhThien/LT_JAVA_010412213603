"use client";

import Image from "next/image";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Navigation } from "swiper/modules";
import ButtonMessage from "@/components/ui/buttonMessage";

const listIntructions = [
  {
    id: 1,
    title: "Khảo Sát, Kiểm Tra, Báo Giá Chi Tiết",
    description: [
      "Nhân viên Kiểm soát Chất lượng & đối tác kỹ thuật kiểm tra model, tình trạng hoạt động của máy",
      "Đo ga bằng đồng hồ đo chuyên dụng",
      "Đề xuất phương án vệ sinh phù hợp & báo giá chi tiết trước khi vệ sinh",
    ],
    image: "/dich-vu/ve-sinh-may-lanh/b1.png",
  },
  {
    id: 2,
    title: "Vệ Sinh Lá Nhôm Của Giàn Lạnh",
    description: [
      "Vệ Sinh Lá Nhôm Của Giàn Lạnh",
      "Bọc giàn lạnh bằng túi trùm chống văng nước",
      "Vệ sinh phần lá nhôm của giàn lạnh bằng vòi xịt cao áp (không để nước bắn vào phần bo mạch)",
    ],
    image: "/dich-vu/ve-sinh-may-lanh/b2.png",
  },
  {
    id: 3,
    title: "Vệ Sinh Vỏ Máy, Màng Lọc & Lau Khô",
    description: [
      "Dùng vòi cao áp xịt rửa vỏ máy, màng lọc",
      "Lau khô màng lọc, vỏ máy và lắp lại như ban đầu",
    ],
    image: "/dich-vu/ve-sinh-may-lanh/b3.png",
  },
  {
    id: 4,
    title: "Vệ Sinh Giàn Nóng (nếu tiếp cận được)",
    description: [
      "Dùng vòi cao áp xịt rửa phần lá nhôm của giàn nóng",
      "Thổi bay bụi bẩn để giàn nóng tản nhiệt tốt hơn, tiết kiệm điện",
    ],
    image: "/dich-vu/ve-sinh-may-lanh/b4.jpg",
  },
  {
    id: 5,
    title: "Kiểm Tra & Nghiệm Thu",
    description: [
      "Kiểm tra chất lượng xử lý đạt chuẩn",
      "Khách hàng kiểm tra & hài lòng với chất lượng xử lý",
      "Ký biên bản nghiệm thu",
    ],
    image: "/dich-vu/ve-sinh-may-lanh/b5.png",
  },
];

const listPrice = [
  {
    id: 1,
    title: "Máy lạnh treo tường",
    description:
      "Đa dạng công suất, model máy. Vệ sinh càng nhiều máy càng tiết kiệm. Luôn tặng kèm voucher giá trị lên đến 80,000đ áp dụng tất cả dịch vụ (cho lần tiếp theo)",
    price: "Từ 220,000đ",
    image: "/dich-vu/ve-sinh-may-lanh/p1.png",
  },
  {
    id: 2,
    title: "Máy lạnh tủ đứng",
    description:
      "Vệ sinh máy lạnh tủ đứng các model khác nhau bởi thợ tay nghề cao. Luôn tặng kèm voucher giá trị lên đến 80,000đ áp dụng tất cả dịch vụ (cho lần tiếp theo)",
    price: "600,000đ",
    image: "/dich-vu/ve-sinh-may-lanh/p2.png",
  },
  {
    id: 3,
    title: "Máy lạnh âm trần",
    description:
      "Vệ sinh máy lạnh treo tường với dụng cụ, thiết bị chuyên dụng. Luôn tặng kèm voucher giá trị lên đến 80,000đ áp dụng tất cả dịch vụ (cho lần tiếp theo)",
    price: "650,000đ",
    image: "/dich-vu/ve-sinh-may-lanh/p3.png",
  },
  {
    id: 4,
    title: "Ga R22",
    description:
      "Giá được tính cho một lần bơm. Luôn đo ga bằng đồng hồ chuyên dụng trước khi tiến hành bơm ga cho khách hàng",
    price: "650,000đ",
    image: "/dich-vu/ve-sinh-may-lanh/p4.png",
  },
  {
    id: 5,
    title: "Ga R410A",
    description:
      "Giá được tính cho một lần bơm. Luôn đo ga bằng đồng hồ chuyên dụng trước khi tiến hành bơm ga cho khách hàng",
    price: "850,000đ",
    image: "/dich-vu/ve-sinh-may-lanh/p5.png",
  },
  {
    id: 6,
    title: "Ga R32",
    description:
      "Giá được tính cho một lần bơm. Luôn đo ga bằng đồng hồ chuyên dụng trước khi tiến hành bơm ga cho khách hàng",
    price: "850,000đ",
    image: "/dich-vu/ve-sinh-may-lanh/p6.png",
  },
];

export default function VeSinhMayLanh() {
  return (
    <div>
      <Image
        src="/dich-vu/giat-say/bg.png"
        alt="Background"
        width="1200"
        height="400"
        className="w-full h-[400px] object-cover"
      />
      <div className="max-w-7xl mx-auto px-4">
        <div className="py-12">
          <div className="text-4xl text-center font-bold">
            Quy trình dịch vụ vệ sinh máy lạnh
          </div>
          <div className="mt-10">
            <Swiper
              grabCursor={true}
              slidesPerView={4}
              navigation={true}
              spaceBetween={20}
              pagination={true}
              modules={[Navigation]}
            >
              {listIntructions.map((item) => {
                return (
                  <SwiperSlide key={item.id}>
                    <div className="rounded-4xl border border-gray-300 w-fit p-5">
                      <Image
                        src={item.image}
                        alt={item.title}
                        width="236"
                        height="187"
                        className="mx-auto mb-5 max-w-[236px] h-[187px] object-cover rounded-4xl"
                      />
                      <div className="flex items-start gap-2 max-w-[236px] h-[85px]">
                        <span className="text-xl font-bold bg-main rounded-full aspect-square w-10 h-10 flex items-center justify-center">
                          {item.id}
                        </span>
                        <div className="text-xl font-bold max-h-[85px]">
                          {item.title}
                        </div>
                      </div>
                      <div>
                        {item.description.map((desc) => {
                          return (
                            <div key={desc} className="flex items-start gap-5">
                              <Image
                                src="/ve-chung-toi/checked.svg"
                                alt="Checked"
                                width="18"
                                height="18"
                              />
                              <span className="text-sm">{desc}</span>
                            </div>
                          );
                        })}
                      </div>
                    </div>
                  </SwiperSlide>
                );
              })}
            </Swiper>
          </div>
        </div>
        <div className="py-12">
          <div className="text-4xl text-center font-bold mb-12">
            Bảng giá dịch vụ vệ sinh máy lạnh
          </div>
          <div className="flex flex-wrap gap-5 justify-center items-center">
            {listPrice.map((item) => {
              return (
                <div
                  key={item.id}
                  className="w-[30%] rounded-[32px] border-transparent border hover:border-gray-200 py-5 flex justify-center items-center cursor-pointer"
                >
                  <div className="w-full max-w-[320px]">
                    <Image
                      src={item.image}
                      alt="image"
                      width="200"
                      height="200"
                      className="w-[200px] h-[200px]"
                    />
                    <div className="mt-5 text-center text-xl font-bold">
                      {item.title}
                    </div>
                    <div className="mt-2 text-center">☑️{item.description}</div>
                    <div className="flex justify-center items-center">
                      <ButtonMessage className="mt-5 mx-auto bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer" />
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}
