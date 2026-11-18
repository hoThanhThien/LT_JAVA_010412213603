import Image from "next/image";
import ButtonMessage from "@/components/ui/buttonMessage";

const listDetail = [
  {
    id: 1,
    image: "/doi-tac/nhan-vien.jpg",
    name: "Cho Nhân Viên Của Bạn",
    description:
      "Dành cho nhân viên của bạn một đặc ân với việc chăm sóc việc giặt ủi & vệ sinh cho họ, giúp họ có thêm thời gian cống hiến cho công ty. Mức giá ưu đãi cho doanh nghiệp!",
  },

  {
    id: 2,
    image: "/doi-tac/khach-san.jpg",
    name: "Nhà Hàng, Khách Sạn, Cafe, Salon, Spa, Massage",
    description:
      "Xử lý việc giặt ủi & vệ sinh cho doanh nghiệp của bạn với dịch vụ chuyên nghiệp của Heramo. Chúng tôi đảm bảo chất lượng cao cấp với mức giá ưu đãi cho doanh nghiệp.",
  },
  {
    id: 3,
    image: "/doi-tac/airbnb.jpg",
    name: "Airbnb Host, Hostel, Private Serviced Apartment, Officetel",
    description:
      "Muốn chăm sóc việc giặt ủi cho khách của mình với chất lượng & sự tiện lợi cao nhất? Liên hệ ngay Heramo để nhận ưu đãi.",
  },
  {
    id: 4,
    image: "/doi-tac/shop.jpg",
    name: "Shop Thời Trang Cao Cấp, Veston, Váy Đầm, Giày, Sofa, Rèm, Nệm",
    description:
      "Tặng khách hàng của bạn những ưu đãi đặc biệt để sử dụng dịch vụ giặt ủi & vệ sinh cao cấp của Heramo. Khiến họ hài lòng & hạnh phúc hơn nữa.",
  },
  {
    id: 5,
    image: "/doi-tac/giat.jpg",
    name: "Tiệm Giặt Ủi, Giặt Hấp",
    description:
      "Sẵn sàng để phục vụ nhiều khách hàng hơn? Hãy cùng Heramo phát triễn việc kinh doanh của bạn.",
  },
];

export default function DoiTac() {
  return (
    <div>
      <div className="relative w-full min-h-[75vh] sm:min-h-[85vh] bg-[url('/doi-tac/hop-tac-cung-heramo.jpg')] bg-cover bg-center bg-no-repeat">
        <div className="absolute inset-0 bg-black/50"></div>
        <div className="relative z-10 text-white flex flex-col justify-center items-center gap-5 px-4 sm:px-8 lg:px-16 py-20 min-h-[75vh] sm:min-h-[85vh] text-center max-w-5xl mx-auto">
          <h1 className="text-3xl sm:text-5xl lg:text-6xl font-bold leading-tight">
            HỢP TÁC CÙNG HERAMO
          </h1>
          <p className="text-base sm:text-xl lg:text-2xl text-gray-100 max-w-3xl">
            HÃY CÙNG HERAMO NÂNG TẦM VIỆC KINH DOANH CỦA BẠN!
          </p>
          <ButtonMessage
            label="LIÊN HỆ NGAY"
            className="cursor-pointer rounded-full bg-main hover:bg-hover px-8 py-3 text-base sm:text-xl font-bold text-black shadow-md transition-colors"
          />
        </div>
      </div>
      <div className="max-w-7xl mx-auto px-4 sm:px-8 lg:px-12 py-12 text-center">
        <div className="flex flex-col justify-center items-center gap-4 sm:gap-5 text-sm sm:text-base text-gray-700 leading-relaxed max-w-4xl mx-auto">
          <div>
            HERAMO là ứng dụng cung cấp dịch vụ giặt ủi & vệ sinh cao cấp theo
            nhu cầu đầu tiên ở Vietnam, được thành lập với sứ mệnh giải phóng
            con người khỏi gánh nặng giặt ủi & công việc gia đình, mang trả lại
            thời gian để họ có thể tận hưởng cuộc sống của mình.
          </div>
          <div>
            Chúng tôi mong muốn được hợp tác cùng bạn, cùng nhau Heramo có thể
            giúp phát triễn việc kinh doanh của bạn, hoặc mang lại cho nó một vẻ
            ngoài tinh tươm & chuyên nghiệp.
          </div>
          <div>
            Dù bạn là một chủ nhà Airbnb muốn chăm sóc việc giặt ủi cho khách
            của mình với chất lượng & sự tiện lợi cao nhất, một cửa hàng kinh
            doanh giày đang muốn tặng voucher cho khách hàng của mình để sử dụng
            dịch vụ chăm sóc vệ sinh giày chuyên nghiệp, hay thậm chí là một
            công ty muốn dành cho nhân viên của mình một đặc ân, chăm sóc toàn
            bộ việc giặt ủi cho họ, giúp nhân viên cảm thấy hạnh phúc và thêm
            cống hiến cho công việc. HERAMO có rất nhiều thứ dành riêng cho bạn!
          </div>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-8 text-center pt-14 sm:pt-20 pb-8">
          {listDetail.map((item) => {
            return (
              <div
                key={item.id}
                className="flex flex-col items-center gap-4 sm:gap-5 rounded-2xl border border-zinc-100 bg-white p-5 shadow-xs"
              >
                <Image src={item.image} alt={item.name} width="120" height="111" />
                <div className="font-semibold text-base sm:text-lg min-h-[3.25rem] flex items-center justify-center">
                  {item.name}
                </div>
                <div className="text-sm text-gray-600 leading-relaxed">
                  {item.description}
                </div>
              </div>
            );
          })}
        </div>
      </div>
      <div className="bg-main">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between gap-6 px-4 sm:px-8 lg:px-12 py-10 sm:py-12 text-center md:text-left">
          <div className="text-black text-xl sm:text-2xl lg:text-3xl font-bold md:max-w-[70%] leading-snug">
            MUỐN PHÁT TRIỂN HOẶC MANG TỚI MỘT VẺ NGOÀI TUYỆT VỜI CHO DOANH
            NGHIỆP CỦA BẠN?
          </div>
          <div className="shrink-0 flex justify-center md:justify-end">
            <ButtonMessage
              label="LIÊN HỆ CHÚNG TÔI"
              className="cursor-pointer rounded-full bg-black text-main font-bold px-7 py-3 text-sm sm:text-base hover:bg-zinc-900 transition-colors"
            />
          </div>
        </div>
      </div>
    </div>
  );
}
