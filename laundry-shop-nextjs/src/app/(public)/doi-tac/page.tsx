import Image from "next/image";

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
      <div className="relative w-full h-auto min-h-screen mt-[-16px] bg-[url('/doi-tac/hop-tac-cung-heramo.jpg')] bg-no-repeat bg-[length:100%_100%]">
        <div className="absolute inset-0 bg-black/40"></div>
        <div className="relative z-10 text-white flex flex-col justify-center items-center gap-5 p-24 min-h-screen">
          <div className="text-6xl">HỢP TÁC CÙNG HERAMO</div>
          <div className="text-2xl">
            HÃY CÙNG HERAMO NÂNG TẦM VIỆC KINH DOANH CỦA BẠN!
          </div>
          <button className="bg-main text-white text-2xl p-3 rounded-lg">
            LIÊN HỆ NGAY
          </button>
        </div>
      </div>
      <div className="max-w-7xl mx-auto px-20 py-12 text-center">
        <div className="flex flex-col justify-center items-center gap-5">
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
        <div className="grid grid-cols-5 gap-10 text-center pt-24 pb-12">
          {listDetail.map((item) => {
            return (
              <div key={item.id} className="flex flex-col items-center gap-10">
                <Image src={item.image} alt="image" width="120" height="111" />
                <div className="font-medium text-lg h-[80px]">{item.name}</div>
                <div className="">{item.description}</div>
              </div>
            );
          })}
        </div>
      </div>
      <div className="bg-main">
        <div className="max-w-7xl mx-auto flex items-center py-12">
          <div className="text-white text-3xl font-medium w-[70%]">
            MUỐN PHÁT TRIỂN HOẶC MANG TỚI MỘT VẺ NGOÀI TUYỆT VỜI CHO DOANH
            NGHIỆP CỦA BẠN?
          </div>
          <div className="w-[30%] flex justify-end">
            <button className="cursor-pointer bg-transparent text-white font-bold rounded-sm border border-white p-3 hover:bg-white hover:text-main">
              LIÊN HỆ CHÚNG TÔI
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
