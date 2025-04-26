"use client";

import Image from "next/image";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Navigation } from "swiper/modules";
import ButtonMessage from "@/components/ui/buttonMessage";

const listIntructions = [
  {
    id: 1,
    title: "Phân Loại, Gắn Tag, Kiểm Tra",
    description: [
      "Bộ phận Kiểm soát Chất Lượng kiểm tra tình trạng, chất liệu giày",
      "Gắn tag định danh, chụp hình, lưu vào hệ thống của HERAMO",
      "Chọn dung dịch vệ sinh giày phù hợp",
    ],
    image: "/dich-vu/ve-sinh-giay/b1.jpg",
  },
  {
    id: 2,
    title: "Vệ Sinh Bên Ngoài, Bên Trong Giày",
    description: [
      "Dùng bàn chải lông siêu mềm, không làm ảnh hưởng đến chất liệu giày",
      "Dung dịch vệ sinh chuyên dụng Jason Markk, Crep Protect",
    ],
    image: "/dich-vu/ve-sinh-giay/b2.jpg",
  },
  {
    id: 3,
    title: "Vệ Sinh Đế Giày, Dây Giày",
    description: [
      "Vệ sinh kỹ phần đế giày bằng bàn chải lông mềm",
      "Ngâm dây giày trong dung dịch vệ sinh đặc biệt & vò nhẹ nhàng",
    ],
    image: "/dich-vu/ve-sinh-giay/b3.jpg",
  },
  {
    id: 4,
    title: "Hấp Khử Mùi",
    description: [
      "Hong khô giày bằng máy hấp chuyên dụng",
      "Khử mùi, diệt khuẩn bằng tia UV",
      "Giày có hương thơm vô cùng dễ chịu",
    ],
    image: "/dich-vu/ve-sinh-giay/b4.jpg",
  },
  {
    id: 5,
    title: "Kiểm Tra Chất Lượng & Đóng Gói",
    description: [
      "Kiểm tra chất lượng xử lý đạt chuẩn",
      "Độn foam để giữ form giày không biến dạng",
      "Đóng gói & giao đến khách hàng",
    ],
    image: "/dich-vu/ve-sinh-giay/b5.jpg",
  },
];

const listPrice = [
  {
    id: 1,
    title: "Vệ sinh giày toàn diện",
    description:
      "100% vệ sinh thủ công bằng dung dịch cao cấp nhập khẩu (Jason Markk, Crep Protect).",
    price: "139.000/đôi",
    image: "/dich-vu/ve-sinh-giay/p1.jpg",
  },
  {
    id: 2,
    title: "Vệ sinh toàn diện giày da, giày tây",
    description:
      "100% giày da, giày tây được chăm sóc bởi đội ngũ kỹ thuật tâm huyết, có tay nghề cao.",
    price: "209.000đ/đôi",
    image: "/dich-vu/ve-sinh-giay/p2.jpg",
  },
  {
    id: 3,
    title: "Vệ sinh toàn diện giày da lộn",
    description:
      "Chất liệu da lộn, nubuck được áp dụng phương pháp giặt giày khô với dung dịch vệ sinh chuyên dụng cao cấp.",
    price: "209.000đ/đôi",
    image: "/dich-vu/ve-sinh-giay/p3.jpg",
  },
  {
    id: 4,
    title: "Giày Siêu Tốc 8H",
    description: "Xử lý cực kỳ nhanh chóng trong 8 giờ.",
    price: "249.000đ/đôi",
    image: "/dich-vu/ve-sinh-giay/p4.jpg",
  },
  {
    id: 5,
    title: "Giày Cấp Tốc 24H",
    description: "Xử lý siêu nhanh trong 24 giờ.",
    price: "209.000đ/đôi",
    image: "/dich-vu/ve-sinh-giay/p5.jpg",
  },
  {
    id: 6,
    title: "Giày Nhanh 48H",
    description: "Xử lý siêu nhanh trong 48 giờ.",
    price: "189.000đ/đôi",
    image: "/dich-vu/ve-sinh-giay/p6.jpg",
  },
  {
    id: 7,
    title: "Vệ sinh toàn diện & tẩy ố thân giày",
    description: "Làm sạch từ 90 - 100% các vết ố vàng, ố màu trên thân giày.",
    price: "319.000đ/đôi",
    image: "/dich-vu/ve-sinh-giay/p7.jpg",
  },
  {
    id: 8,
    title: "Vệ sinh toàn diện & sơn đế giày",
    description:
      "Giải pháp toàn diện nhất để khôi phục 100% tình trạng đế giày bị ố vàng, ngả màu (đặc biệt là các dòng giày Nike AF1, Adidas Boost).",
    price: "539.000đ/đôi",
    image: "/dich-vu/ve-sinh-giay/p8.jpg",
  },
  {
    id: 9,
    title: "Vệ sinh toàn diện & sơn nhuộm giày",
    description:
      "Phục hồi giày cũ bằng sơn Angelus cao cấp nhập khẩu, mang đến vẻ đẹp tự nhiên và bền theo thời gian.",
    price: "639.000đ/đôi",
    image: "/dich-vu/ve-sinh-giay/p9.jpg",
  },
];

export default function VeSinhGiay() {
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
              HERAMO - Vệ sinh giày giặt giày cao cấp 4.0
            </div>
            <div className="flex flex-col gap-4 text-base font-medium my-5">
              <div>
                HERAMO - Thương hiệu tiên phong & dẫn đầu trong ngành vệ sinh
                giày, chăm sóc, spa phục hồi & sửa chữa giày tại TP. Hồ Chí
                Minh. Với hơn 7 năm kinh nghiệm, 10 chi nhánh và được tin dùng
                bởi hơn 136.000+ khách hàng, chúng tôi cam kết cam kết mang đến
                cho bạn trải nghiệm khác biệt - tiện lợi, chất lượng và tuyệt
                đối an tâm.
              </div>
              <div>
                HERAMO cung cấp đầy đủ các dịch vụ từ vệ sinh, chăm sóc, giặt
                hấp giày đến sơn đế, tẩy ố, xịt nano chống thấm, dán sole, dán
                keo, may đế. Mỗi đôi giày đều được chăm sóc thủ công 100% bởi
                đội ngũ kỹ thuật viên chuyên nghiệp cùng quy trình xử lý riêng
                biệt và sử dụng dung dịch vệ sinh cao cấp. Nhằm đảm bảo giày
                sạch hoàn toàn và không gây ảnh hưởng đến chất liệu, màu sắc,
                form dáng. Đồng thời, công nghệ Barcode độc quyền được tích hợp
                trên từng đôi giày, giúp theo dõi chính xác từng bước trong quy
                trình xử lý.
              </div>
              <div>
                Đặt dịch vụ ngay trên các kênh online hoặc tại hệ thống 10 cửa
                hàng của chúng tôi, theo dõi đơn hàng mọi lúc mọi nơi tiện lợi
                với mobile app của HERAMO. Hãy trải nghiệm để thấy sự khác biệt
                ngay!
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
            Bảng giá dịch vụ vệ sinh giày
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
                      width="260"
                      height="200"
                      className="w-[260px] h-[200px]"
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
