"use client";

import Image from "next/image";
import { useRouter } from "next/navigation";

export default function Services() {
  const router = useRouter();
  return (
    <div className="max-w-7xl mx-auto px-4 pb-20">
      <div className="text-4xl text-center font-bold">
        Các dịch vụ tại <span className="text-main">HERAMO</span>
      </div>
      <div className="text-xl mt-2 mb-16 block mx-auto text-center max-w-[600px] text-gray-600">
        Đặt ngay dịch vụ tại website, mobile app hoặc tại cửa hàng của HERAMO.
        Giao nhận tận nơi, theo dõi đơn hàng & món đồ mọi lúc.
      </div>
      <div className="flex flex-wrap justify-center gap-5 mx-auto">
        <div
          onClick={() => {
            router.push("dich-vu/giat-hap-giat-kho");
          }}
          className="w-[30%] rounded-[32px] border-transparent border hover:border-gray-200 py-5 flex justify-center items-center cursor-pointer"
        >
          <div className="w-full max-w-[320px]">
            <Image
              src="/service-1.png"
              alt="Giặt hấp, giặt khô"
              width="0"
              height="0"
              sizes="100vw"
              className="w-full h-[180px]"
            />
            <div className="mt-5 text-center text-xl font-bold">
              Giặt hấp, giặt khô
            </div>
            <div className="mt-2 text-center">
              Chăm sóc tủ đồ với quy trình giặt hấp, giặt khô đúng chuẩn. Máy
              móc hiện đại và dung dịch chuyên dụng
            </div>
            <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
              Xem chi tiết
              <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
            </button>
          </div>
        </div>
        <div
          onClick={() => {
            router.push("dich-vu/giat-say");
          }}
          className="w-[30%] rounded-[32px] border-transparent border hover:border-gray-200 py-5 flex justify-center items-center cursor-pointer"
        >
          <div className="w-full max-w-[320px]">
            <Image
              src="/service-2.png"
              alt="Giặt hấp, giặt khô"
              width="0"
              height="0"
              sizes="100vw"
              className="w-full h-[180px]"
            />
            <div className="mt-5 text-center text-xl font-bold">Giặt sấy</div>
            <div className="mt-2 text-center">
              Giao và nhận lại đồ sạch thơm chỉ trong 24H. Giặt sấy máy riêng.
              Gói giặt theo tháng tiết kiệm
            </div>
            <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
              Xem chi tiết
              <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
            </button>
          </div>
        </div>
        <div
          onClick={() => {
            router.push("dich-vu/ve-sinh-giay");
          }}
          className="w-[30%] rounded-[32px] border-transparent border hover:border-gray-200 py-5 flex justify-center items-center cursor-pointer"
        >
          <div className="w-full max-w-[320px]">
            <Image
              src="/service-3.png"
              alt="Giặt hấp, giặt khô"
              width="0"
              height="0"
              sizes="100vw"
              className="w-full h-[180px]"
            />
            <div className="mt-5 text-center text-xl font-bold">
              Vệ sinh giày
            </div>
            <div className="mt-2 text-center">
              Đầy đủ các dịch vụ vệ sinh, phục hồi chuyên sâu. Chăm sóc giày
              toàn diện, chu đáo
            </div>
            <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
              Xem chi tiết
              <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
            </button>
          </div>
        </div>
        <div className="w-[30%] rounded-[32px] border-transparent border hover:border-gray-200 py-5 flex justify-center items-center cursor-pointer">
          <div className="w-full max-w-[320px]">
            <Image
              src="/service-4.png"
              alt="Giặt hấp, giặt khô"
              width="0"
              height="0"
              sizes="100vw"
              className="w-full h-[180px]"
            />
            <div className="mt-5 text-center text-xl font-bold">
              Vệ sinh spa túi xách
            </div>
            <div className="mt-2 text-center">
              Đa dạng các dịch vụ cao cấp: vệ sinh spa túi, sơn nhuộm, sửa túi,
              phục hồi,... Khôi phục vẻ đẹp hoàn hảo bởi các kỹ thuật viên
              chuyên nghiệp
            </div>
            <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
              Xem chi tiết
              <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
            </button>
          </div>
        </div>
        <div
          onClick={() => {
            router.push("dich-vu/ve-sinh-giat-sofa-nem-rem-tham");
          }}
          className="w-[30%] rounded-[32px] border-transparent border hover:border-gray-200 py-5 flex justify-center items-center cursor-pointer"
        >
          <div className="w-full max-w-[320px]">
            <Image
              src="/service-5.jpg"
              alt="Giặt hấp, giặt khô"
              width="0"
              height="0"
              sizes="100vw"
              className="w-full h-[180px]"
            />
            <div className="mt-5 text-center text-xl font-bold">
              Vệ sinh sofa, nệm, rèm, thảm
            </div>
            <div className="mt-2 text-center">
              Bảo vệ sức khỏe cả gia đình với dịch vụ vệ sinh nhà cửa định kỳ.
              Kỹ thuật viên chuyên nghiệp, giàu kinh nghiệm
            </div>
            <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
              Xem chi tiết
              <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
            </button>
          </div>
        </div>
        <div
          onClick={() => {
            router.push("dich-vu/ve-sinh-may-lanh");
          }}
          className="w-[30%] rounded-[32px] border-transparent border hover:border-gray-200 py-5 flex justify-center items-center cursor-pointer"
        >
          <div className="w-full max-w-[320px]">
            <Image
              src="/service-6.png"
              alt="Giặt hấp, giặt khô"
              width="0"
              height="0"
              sizes="100vw"
              className="w-full h-[180px]"
            />
            <div className="mt-5 text-center text-xl font-bold">
              Vệ sinh máy lạnh
            </div>
            <div className="mt-2 text-center">
              Cải thiện chất lượng không khí, nâng cao sức khỏe gia đình. Đầy đủ
              dịch vụ vệ sinh, bơm ga các dòng máy
            </div>
            <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
              Xem chi tiết
              <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
