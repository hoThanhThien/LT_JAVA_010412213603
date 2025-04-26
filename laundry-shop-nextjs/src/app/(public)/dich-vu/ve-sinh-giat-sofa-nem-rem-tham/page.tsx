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
      "Nhân viên Kiểm soát Chất lượng & đối tác kỹ thuật kiểm tra tình trạng, chất liệu món đồ",
      "Đề xuất phương án vệ sinh phù hợp & báo giá chi tiết trước rõ ràng khi vệ sinh",
    ],
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/b1.jpg",
  },
  {
    id: 2,
    title: "Vệ Sinh Bụi Bẩn Trên Bề Mặt",
    description: [
      "Hút sạch bụi bẩn bám trên bề mặt trước khi vệ sinh bằng máy hút công suất lớn",
    ],
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/b2.png",
  },
  {
    id: 3,
    title: "Vệ Sinh Chuyên Sâu & Diệt Khuẩn",
    description: [
      "Xịt dung dịch vệ sinh lên bề mặt, dùng máy móc chuyên dụng đánh bay các vết bẩn",
      "Hút toàn bộ vết bẩn cùng dung dịch",
      "Lặp lại các bước trên nhiều lần đến khi đạt được độ sạch tối đa",
    ],
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/b3.png",
  },
  {
    id: 4,
    title: "Thổi Khô ( Áp dụng vệ sinh từ 5 món trở lên)",
    description: [
      "Dùng máy thổi công suất lớn thổi khô bề mặt",
      "Có thể sử dụng ngay sau khi vệ sinh từ 3 - 5 tiếng",
    ],
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/b4.png",
  },
  {
    id: 5,
    title: "Kiểm Tra & Nghiệm Thu",
    description: [
      "Kiểm tra chất lượng xử lý đạt chuẩn",
      "Khách hàng kiểm tra & hài lòng với chất lượng xử lý",
      "Ký biên bản nghiệm thu",
    ],
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/b5.png",
  },
];

const listPrice = [
  {
    id: 1,
    title: "Giặt ghế sofa",
    description: "Vệ sinh sofa đơn, sofa băng, sofa chữ L, sofa chữ U.",
    price: "150,000đ - 490,000đ",
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/p1.png",
  },
  {
    id: 2,
    title: "",
    description: "",
    price: "",
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/p2.png",
  },
  {
    id: 3,
    title: "Giặt thảm",
    description: "Giặt thảm trang trí, thảm văn phòng.",
    price: "Từ 25,000đ/m²",
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/p3.png",
  },
  {
    id: 4,
    title: "Giặt nệm, topper",
    description: "Vệ sinh nệm cao su/ lò xo/ bông ép.",
    price: "390,000đ",
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/p4.png",
  },
  {
    id: 5,
    title: "Gối ôm, gối nhỏ, gối sofa",
    description: "Vệ sinh gối sofa, gối giường, gối ôm, gối đệm ghế Papasan.",
    price: "Từ 50,000đ",
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/p5.png",
  },
  {
    id: 6,
    title: "Giặt rèm",
    description: "Giá tính theo kg. Áp dụng khi giặt rèm > 20kg.",
    price: "Từ 45,000đ/kg",
    image: "/dich-vu/ve-sinh-giat-sofa-nem-rem-tham/p6.png",
  },
];

export default function VeSinhSofa() {
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
        <div className="flex gap-20 justify-center items-center py-12 px-24">
          <Image
            src="/dich-vu/banner.jpg"
            alt="Banner"
            width="400"
            height="400"
          />
          <div className="w-[60%]">
            <div className="text-4xl font-bold">
              HERAMO - Vệ sinh sofa, nệm, rèm, thảm cao cấp 4.0
            </div>
            <div className="flex flex-col gap-4 text-base font-medium my-5">
              <div>
                HERAMO tự hào là đơn vị giặt ghế sofa, nệm, rèm, thảm cao cấp
                hàng đầu khu vực TP. Hồ Chí Minh. Đồ của bạn sẽ được xử lý
                chuyên nghiệp, bài bản theo từng loại chất liệu, kiểu dáng với
                dung dịch vệ sinh nhập khẩu đảm bảo độ an toàn tuyệt đối với
                người dùng & thân thiện với chất liệu vải và môi trường.
              </div>
              <div>
                Chúng tôi luôn đưa ra các giải pháp vệ sinh tối ưu và sẵn sàng
                công khai minh giá cả để khách hàng có sự lựa chọn đúng đắn
                nhất. Với gần 6 năm kinh nghiệm và được tin dùng bởi hơn
                136.000+ khách hàng, HERAMO cam kết mang đến cho bạn trải nghiệm
                khác biệt - hoàn toàn tiện lợi, chất lượng vượt trội và an tâm
                tuyệt đối.
              </div>
              <div>
                Hệ thống chuỗi cửa hàng và nhượng quyền của HERAMO trải dài khắp
                TP. Hồ Chí Minh và đang có 10 chi nhánh.
              </div>
            </div>
            <ButtonMessage className="bg-black text-white hover:bg-transparent hover:text-black border border-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[50%] " />
          </div>
        </div>
        <div className="py-12">
          <div className="text-4xl text-center font-bold">
            Quy trình vệ sinh tiêu chuẩn
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
            Bảng giá dịch vụ vệ sinh sofa, nệm, rèm, thảm
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
