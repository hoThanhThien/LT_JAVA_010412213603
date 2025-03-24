"use client";

import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/swiper-bundle.css";
import { Navigation } from "swiper/modules";
import Image from "next/image";
import Link from "next/link";

const listNews = [
  {
    id: 1,
    title: "Người Đô Thị, 12/10/2023",
    description:
      "Lê Phước Phúc - CEO/Founder HERAMO - Người giặt giày số 1 Việt Nam",
    logo: "/news/nguoi-do-thi.png",
    link: "https://nguoidothi.net.vn/phuc-heramo-nguoi-giat-giay-so-1-viet-nam-41218.html",
  },
  {
    id: 2,
    title: "Người lao động, 14/08/2023",
    description: "Heramo - Chuyên nghiệp hóa ngành giặt ủi",
    logo: "/news/nguoi-lao-dong.png",
    link: "https://nld.com.vn/khoi-nghiep/chuyen-nghiep-hoa-nganh-giat-ui-20230813202803661.htm",
  },
  {
    id: 3,
    title: "Vnexpress, 11/01/2023",
    description:
      "Heramo - nền tảng số cho ngành giặt ủi, thương hiệu dẫn đầu ở TP HCM",
    logo: "/news/vn-express.png",
    link: "https://vnexpress.net/heramo-nen-tang-so-cho-nganh-giat-ui-4558351.html",
  },
  {
    id: 4,
    title: "CafeF, 12/01/2023",
    description: `Nhượng quyền thương hiệu ngành "giặt ủi" - hướng đi mới cho nhà đầu tư`,
    logo: "/news/cafef.png",
    link: "https://cafef.vn/nhuong-quyen-thuong-hieu-nganh-giat-ui-huong-di-moi-cho-nha-dau-tu-20230111222129826.chn",
  },
  {
    id: 5,
    title: "Kênh 14, 11/01/2023",
    description: "Bí quyết tiết kiệm thời gian và chi phí cho mùa Tết",
    logo: "/news/kenh-14.png",
    link: "https://kenh14.vn/lam-som-3-viec-nay-de-co-mot-cai-tet-binh-an-dieu-cuoi-cung-con-giup-ban-tiet-kiem-tien-20230111092202601.chn",
  },
  {
    id: 6,
    title: "Dân trí, 21/01/2022",
    description:
      "Đón Tết online kiểu người trẻ: tiện lợi, thảnh thơi nhưng vẫn đầy ý nghĩa!",
    logo: "/news/dan-tri.png",
    link: "https://dantri.com.vn/kinh-doanh/don-tet-kieu-online-cua-nguoi-tre-20220121070609106.htm",
  },
  {
    id: 7,
    title: "CafeF, 22/11/2021",
    description:
      "HERAMO - Startup tham vọng cách mạng hóa trải nghiệm giặt ủi, vệ sinh",
    logo: "/news/cafef.png",
    link: "https://cafef.vn/heramo-startup-tham-vong-cach-mang-hoa-trai-nghiem-giat-ui-ve-sinh-20211119172935415.chn",
  },
  {
    id: 8,
    title: "Kênh 14, 19/01/2022",
    description:
      "Chưa hết HOT: Ngoài mang tiền về cho mẹ, chúng ta còn có thể mang gì?",
    logo: "/news/kenh-14.png",
    link: "https://kenh14.vn/chua-het-hot-ngoai-mang-tien-ve-cho-me-thi-con-mang-gi-20220119002036942.chn",
  },
  {
    id: 9,
    title: "Afamily, 20/01/2022",
    description:
      "Làm ngay 4 đầu việc này nếu bạn mãi bận rộn chưa kịp chuẩn bị Tết!",
    logo: "/news/a-family.png",
    link: "https://afamily.vn/lam-ngay-4-dau-viec-nay-neu-ban-mai-ban-ron-chua-kip-chuan-bi-tet-20220120161048169.chn",
  },
  {
    id: 10,
    title: "CafeBiz, 15/11/2021",
    description:
      "HERAMO - Ứng dụng giặt ủi, vệ sinh 4.0 đạt Top 3 Startup Wheel 2021",
    logo: "/news/cafe-biz.png",
    link: "https://cafebiz.vn/heramo-ung-dung-giat-ui-ve-sinh-40-dat-top-3-startup-wheel-2021-20211115122448562.chn",
  },
  {
    id: 11,
    title: "Nhịp sống Kinh tế, 15/11/2021",
    description:
      "HERAMO - Ứng dụng giặt ủi, vệ sinh 4.0 đạt Top 3 Startup Wheel 2021",
    logo: "/news/nhip-song-kinh-te.png",
    link: "https://nhipsongkinhte.toquoc.vn/heramo-ung-dung-giat-ui-ve-sinh-40-dat-top-3-startup-wheel-2021-520211511122513165.htm",
  },
];

export default function News() {
  return (
    <Swiper
      grabCursor={true}
      slidesPerView={4}
      navigation={true}
      spaceBetween={20}
      modules={[Navigation]}
    >
      {listNews.map((item) => {
        return (
          <SwiperSlide key={item.id}>
            <div className="rounded-4xl border border-gray-300 p-5">
              <Image
                src={item.logo}
                alt={item.title}
                width="218"
                height="48"
                className="max-w-[218px] max-h-[48px] mx-auto mb-5"
              />
              <div className="text-sm text-gray-500 max-w-[218px] h-[110px]">
                {item.description}
              </div>
              <div className="max-w-[218px] h-[24px] font-bold">
                {item.title}
              </div>
              <Link href={item.link} target="_blank">
                <button className="flex items-center mt-5 gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
                  Đọc bài viết
                  <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
                </button>
              </Link>
            </div>
          </SwiperSlide>
        );
      })}
    </Swiper>
  );
}
