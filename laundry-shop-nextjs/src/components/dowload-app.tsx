import Image from "next/image";

export default function DownloadApp() {
  return (
    <div className="grid grid-cols-2 bg-[url('/home/phone-tien-trinh.jpg')] bg-center bg-no-repeat bg-cover h-[1124px] px-24 my-12 mt-[-48px]">
      <div className="col-span-1 col-start-1 self-end">
        <Image
          src={"/home/homescreen.png"}
          alt="home-screen"
          width="565"
          height="976"
          // className="w-[40%] h-[290px] mx-auto"
        />
      </div>
      <div className="col-span-1 col-start-2 self-end h-full flex flex-col justify-center items-center gap-15">
        <div className="text-4xl font-bold">Tải App Ngay - Ưu Đãi Về Tay!</div>
        <div className="text-xl mt-2">
          Đặt tất cả dịch vụ giặt ủi, vệ sinh nhanh chóng. Theo dõi đơn hàng,
          hình ảnh món đồ mọi lúc mọi nơi. Ưu đãi lên đến 100K dành tặng khách
          mới và khách hàng thân thiết
        </div>
        <div className="flex gap-6">
          <Image src="/app-store.svg" alt="AppStore" width="200" height="200" />
          <Image
            src="/google-play.svg"
            alt="GooglePlay"
            width="200"
            height="200"
          />
        </div>
      </div>
    </div>
  );
}
