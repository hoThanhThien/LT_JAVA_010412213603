"use client";

import Image from "next/image";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Navigation } from "swiper/modules";
import ButtonMessage from "@/components/ui/buttonMessage";

const listIntructions = [
  {
    id: 1,
    title: "Tư Vấn & Đặt Lịch Giao Nhận Tận Nơi",
    description: [
      "Khách hàng chọn gói giặt sấy / giặt ủi phù hợp (giặt lẻ, giặt combo)",
      "Chuyên viên chăm sóc khách hàng liên hệ tư vấn & đặt lịch giao nhận",
      "Shipper HERAMO đến tận nơi nhận đồ theo giờ đã hẹn",
    ],
    image: "/dich-vu/giat-say/LD-1.png",
  },
  {
    id: 2,
    title: "Kiểm Tra, Phân Loại, Tẩy Điểm Dơ",
    description: [
      "Bộ phận Kiểm soát Chất lượng phân loại theo chất liệu, màu sắc áo quần & đề xuất phương pháp giặt phù hợp",
      "Chụp hình, lưu vào hệ thống của HERAMO",
      "Tẩy điểm các vết bẩn, vết ố cứng đầu trước khi đưa vào giặt",
    ],
    image: "/dich-vu/giat-say/LD-2.png",
  },
  {
    id: 3,
    title: "Giặt & Sấy Khô",
    description: [
      "Thiết bị giặt, sấy hiện đại (đảm bảo mỗi khách một máy)",
      "Sử dụng nước giặt, nước xả từ những thương hiệu uy tín, thân thiện với người dùng",
    ],
    image: "/dich-vu/giat-say/LD-3.jpg",
  },
  {
    id: 4,
    title: "Kiểm Tra Chất Lượng, Xếp Gọn, Ủi Treo",
    description: [
      "Kiểm tra chất lượng giặt sấy đạt chuẩn",
      "Xếp gọn và đóng gói kỹ lưỡng",
      "Ủi theo yêu cầu của khách (nếu có), treo thẳng & đóng gói",
    ],
    image: "/dich-vu/giat-say/LD-4.png",
  },
  {
    id: 5,
    title: "Giao lại đồ được vệ sinh sạch thơm",
    description: ["Giao lại đồ được vệ sinh sạch thơm"],
    image: "/dich-vu/giat-say/LD-5.png",
  },
];

const listPrice = [
  {
    id: 1,
    title: "Ủi xếp",
    description: "Ủi thẳng đồ và đóng bao khi giao. Tính theo món đồ",
    price: "15,000đ",
    image: "/dich-vu/giat-say/p1.png",
  },
  {
    id: 2,
    title: "Ủi treo",
    description:
      "Ủi treo các loại áo sơ mi, quần tây, đầm váy, áo khoác. Tính theo món đồ",
    price: "Từ 30,000đ - 80,000đ",
    image: "/dich-vu/giat-say/p2.png",
  },
];

export default function GiatSay() {
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
              HERAMO - Giặt hấp giặt khô cao cấp 4.0
            </div>
            <div className="flex flex-col gap-4 text-base font-medium my-5">
              <div>
                HERAMO - Địa chỉ tin cậy cho dịch vụ giặt hấp giặt khô hàng đầu
                tại TP. Hồ Chí Minh. Với 7 năm kinh nghiệm, 10 chi nhánh và được
                tin dùng bởi hơn 136.000+ khách hàng, chúng tôi cam kết cam kết
                mang đến cho bạn trải nghiệm khác biệt - hoàn toàn tiện lợi,
                chất lượng vượt trội và an tâm tuyệt đối.
              </div>
              <div>
                Quần áo của bạn sẽ được chăm sóc bằng quy trình giặt khô đúng
                chuẩn với trang thiết bị máy móc hiện đại. Áp dụng quy trình xử
                lý riêng biệt cho từng chất liệu và sử dụng dung môi giặt khô
                cao cấp nhập khẩu. Nhằm đảm bảo quần áo sạch thơm và không gây
                ảnh hưởng đến chất liệu, màu sắc, form dáng.
              </div>
              <div>
                HERAMO tự hào là đơn vị tiên phong trong việc ứng dụng công nghệ
                4.0 để cách mạng hóa trải nghiệm giặt ủi, vệ sinh truyền thống.
                Hệ thống chuỗi cửa hàng và nhượng quyền của HERAMO trải dài khắp
                TP. Hồ Chí Minh và đang có 10 chi nhánh. Hãy trải nghiệm sự khác
                biệt cùng HERAMO ngay hôm nay!
              </div>
            </div>
            <ButtonMessage className="bg-black text-white hover:bg-transparent hover:text-black border border-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[50%] " />
          </div>
        </div>
        <div className="py-12">
          <div className="text-4xl text-center font-bold">
            Quy trình giặt sấy tiêu chuẩn
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
            Bảng giá dịch vụ giặt sấy
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
