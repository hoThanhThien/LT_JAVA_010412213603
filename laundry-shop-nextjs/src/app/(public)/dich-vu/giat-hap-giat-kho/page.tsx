"use client";

import Image from "next/image";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Navigation } from "swiper/modules";
import ButtonMessage from "@/components/ui/buttonMessage";

const listIntructions = [
  {
    id: 1,
    title: "Phân Loại, Gắn Tag, Kiểm Tra, Tẩy Điểm Dơ",
    description: [
      "Bộ phận Kiểm soát Chất Lượng phân loại theo chất liệu & hướng dẫn giặt ủi từng món đồ",
      "Gắn tag định danh, chụp hình, lưu vào hệ thống của HERAMO",
      "Tẩy điểm các vết bẩn, vết ố cứng đầu trước khi đưa vào giặt hấp",
    ],
    image: "/dich-vu/giat-hap-giat-kho/b1.jpg",
  },
  {
    id: 2,
    title: "Giặt Hấp (Dry Cleaning) & Xả Vắt",
    description: [
      "Máy giặt hấp chuyên dụng, hiện đại",
      "Sử dụng dung môi giặt khô không chứa PERC thân thiện với người dùng",
    ],
    image: "/dich-vu/giat-hap-giat-kho/b2.jpg",
  },
  {
    id: 3,
    title: "Thổi Phom",
    description: ["Máy thổi phom chuyên dụng, khôi phục phom như áo mới"],
    image: "/dich-vu/giat-hap-giat-kho/b3.jpg",
  },
  {
    id: 4,
    title: "Ủi & Pressing",
    description: [
      "Ủi bằng bàn ủi hơi nước",
      "Ủi bằng máy pressing chuyên dụng",
    ],
    image: "/dich-vu/giat-hap-giat-kho/b4.jpg",
  },
  {
    id: 5,
    title: "Giao lại đồ được vệ sinh sạch thơm",
    description: [
      "Kiểm tra chất lượng xử lý đạt chuẩn",
      "Treo thẳng thớm, đóng gói kỹ lưỡng",
      "Giao đến khách hàng bằng thùng chữ U để giữ phom đồ",
    ],
    image: "/dich-vu/giat-hap-giat-kho/b5.jpg",
  },
];

const listPrice = [
  {
    id: 1,
    title: "Giặt hấp áo vest, áo dài, váy cưới",
    description: "Áo quần có chất liệu đặc biệt, họa tiết ren cườm, lông vũ",
    price: "Từ 80,000đ - 210,000đ",
    image: "/dich-vu/giat-hap-giat-kho/p1.png",
  },
  {
    id: 2,
    title: "Giặt hấp sơ mi, áo khoác",
    description: "Áo thun, sơ mi, áo khoác và quần tây, jean.",
    price: "Từ 70,000đ - 200,000đ",
    image: "/dich-vu/giat-hap-giat-kho/p2.png",
  },
  {
    id: 3,
    title: "Giặt hấp chăn mền, thú nhồi bông",
    description:
      "Dung dịch giặt khô an toàn cho sức khỏe cả gia đình, sạch sâu diệt khuẩn, hương thơm dễ chịu.",
    price: "Từ 60,000đ",
    image: "/dich-vu/giat-hap-giat-kho/p3.jpg",
  },
  {
    id: 4,
    title: "Vệ sinh túi xách, balo, vali",
    description:
      "Chăm sóc toàn diện ví da, túi da cao cấp bởi thợ thủ công tay nghề chuyên nghiệp.",
    price: "Từ 100,000đ - 500,000đ",
    image: "/dich-vu/giat-hap-giat-kho/p4.png",
  },
  {
    id: 5,
    title: "Giặt hấp các bộ đồ đặc biệt",
    description:
      "Các loại đồ đặc biệt như găng tay boxing, áo quần mô tô, trang phục Kimono, Hanbok v.v...",
    price: "Từ 170,000đ - 300,000đ",
    image: "/dich-vu/giat-hap-giat-kho/p5.png",
  },
  {
    id: 6,
    title: "Giặt hấp các phụ kiện khác",
    description:
      "Giặt khô, chăm sóc tỉ mỉ từng chi tiết các phụ kiện đi kèm như cà vạt, dây nịt, khăn choàng cổ, mũ nón v.v...",
    price: "Từ 45,000đ - 120,000đ",
    image: "/dich-vu/giat-hap-giat-kho/p6.png",
  },
];

export default function GiatHap() {
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
            Bảng giá dịch vụ giặt hấp
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
