import Image from "next/image";

export default function VeChungToi() {
  return (
    <main className="relative">
      <div className="bg-[url('/home/bg-1.png')] bg-no-repeat min-h-[960px] px-4 bg-[length:100%_1060px]">
        <div className="max-w-7xl mx-auto px-4 flex justify-between pt-[52px]">
          <div className="w-[41%] text-left">
            <div className="text-4xl font-bold">
              Hệ thống nhượng quyền & ứng dụng giặt ủi vệ sinh cao cấp 4.0
            </div>
            <div className="text-xl mt-3">
              HERAMO tiên phong tại Việt Nam trong việc ứng dụng công nghệ 4.0
              để cách mạng hóa trải nghiệm giặt ủi & vệ sinh chỉ với một chạm
              cho hàng triệu người.
            </div>
            <div className="text-xl mt-3">
              Được tin dùng bởi hơn 136.000+ khách hàng, chúng tôi cam kết mang
              đến cho khách hàng trải nghiệm vượt mong đợi, chất lượng vượt trội
              & tuyệt đối an tâm.
            </div>
          </div>
          <div className="w-[50%]">
            <Image
              src="/home/phone-banner-desktop.png"
              alt="Banner"
              width="0"
              height="0"
              sizes="100vw"
              className="w-full h-[552px]"
            />
          </div>
        </div>
      </div>
      <div className="max-w-7xl mx-auto px-24">
        <div className="grid lg:grid-cols-4 gap-10 my-24">
          <div className="lg:col-span-1 lg:col-start-1 text-[28px] font-bold">
            Vì sao chúng tôi xây dựng <span className="text-main">HERAMO</span>?
          </div>
          <div className="lg:col-span-3 lg:col-start-2 text-gray-600 flex flex-col gap-5">
            <div>
              Vào đầu năm 2017, chúng tôi bắt gặp gặp hình ảnh một người phụ nữ
              trước một tiệm giặt, lỉnh kỉnh đồ chợ búa trên xe với một đứa bé
              nhỏ, kèm theo là mấy bịch áo quần, chăn ga gối. Hình ảnh người phụ
              nữ vất vả xoay xở chừng ấy thứ và thêm đứa bé đã đánh mạnh vào tâm
              trí chúng tôi.
            </div>
            <div>
              Từ hôm đó, chúng tôi luôn bị thôi thúc bởi hình ảnh hàng triệu
              người mẹ, người vợ dành hết cuộc đời mình để chăm sóc cho gia đình
              của mình, họ không có thời gian cho riêng mình. Mẹ tôi là một
              trong số họ, vợ tôi cũng vậy! Thật ngạc nhiên khi biết rằng, chúng
              ta sẽ mất 6 năm dành cho những công việc giặt ủi, vệ sinh trong
              suốt quãng đời của mình.
            </div>
            <div>
              Với khát khao mang lại những giá trị tích cực cho cộng đồng, chúng
              tôi quyết định hiện thực hóa ý tưởng xây dựng một ứng dụng giặt
              ủi, vệ sinh cực kỳ tiện lợi. Chúng tôi tin rằng mọi người xứng
              đáng được tận hưởng cuộc sống thay vì dành thời gian cho những
              việc nhàm chán.
            </div>
          </div>
        </div>
        <div className="grid lg:grid-cols-4 gap-10">
          <div className="lg:col-span-1 lg:col-start-1 text-[28px] font-bold">
            Sứ mệnh của <span className="text-main">HERAMO</span>
          </div>
          <div className="lg:col-span-3 lg:col-start-2 text-gray-600 flex flex-col gap-5">
            <div>
              HERAMO là viết tắt của HERA-MOTHER: Hera là Nữ thần Hy Lạp về Hôn
              nhân, Mẹ và Gia đình. Chúng tôi muốn mang lại hạnh phúc cho hàng
              triệu gia đình
            </div>
            <div>
              Sứ mệnh của chúng tôi chính là giải phóng mọi người khỏi gánh nặng
              giặt ủi & công việc gia đình, mang trả lại thời gian để họ có thể
              tận hưởng cuộc sống của mình.
            </div>
          </div>
        </div>
        <div className="flex justify-around mt-20">
          <div>
            <Image
              src="/ve-chung-toi/why_us.jpg"
              alt="Why us"
              width="343"
              height="217"
              className="rounded-[20px] max-w-[343px]"
            />
          </div>
          <div>
            <Image
              src="/ve-chung-toi/su_menh.jpg"
              alt="Why us"
              width="343"
              height="217"
              className="rounded-[20px] max-w-[343px]"
            />
          </div>
        </div>
      </div>
      <div className="max-w-7xl mx-auto px-4 py-24">
        <div className="text-4xl font-bold text-center mb-20">
          3 Giá Trị Vượt Trội của <span className="text-main">HERAMO</span>
        </div>
        <div className="grid lg:grid-cols-3 gap-10">
          <div>
            <Image
              src="/ve-chung-toi/heramo-tien-loi-1.png"
              alt="Giá trị 1"
              width="120"
              height="120"
            />
            <div>
              <div className="text-4xl font-bold mb-3 mt-5">Tiện lợi</div>
              <div className="flex flex-col gap-4">
                <div className="flex items-start gap-5">
                  <Image
                    src="/ve-chung-toi/checked.svg"
                    alt="Checked"
                    width="32"
                    height="32"
                  />
                  <span className="text-xl">
                    Đặt tất cả dịch vụ giặt ủi vệ sinh chỉ với 1 chạm
                  </span>
                </div>
                <div className="flex items-start gap-5">
                  <Image
                    src="/ve-chung-toi/checked.svg"
                    alt="Checked"
                    width="32"
                    height="32"
                  />
                  <span className="text-xl">Giao nhận tận cửa nhà</span>
                </div>
              </div>
            </div>
          </div>
          <div>
            <Image
              src="/ve-chung-toi/giat_ui_1.png"
              alt="Giá trị 2"
              width="120"
              height="120"
            />
            <div>
              <div className="text-4xl font-bold mb-3 mt-5">Tiện lợi</div>
              <div className="flex flex-col gap-4">
                <div className="flex items-start gap-5">
                  <Image
                    src="/ve-chung-toi/checked.svg"
                    alt="Checked"
                    width="32"
                    height="32"
                  />
                  <span className="text-xl">
                    Hệ thống đối tác hàng đầu được tuyển chọn nghiêm ngặt
                  </span>
                </div>
                <div className="flex items-start gap-5">
                  <Image
                    src="/ve-chung-toi/checked.svg"
                    alt="Checked"
                    width="32"
                    height="32"
                  />
                  <span className="text-xl">
                    Công nghệ 5 bước kiểm soát chất lượng nghiêm ngặt
                  </span>
                </div>
                <div className="flex items-start gap-5">
                  <Image
                    src="/ve-chung-toi/checked.svg"
                    alt="Checked"
                    width="32"
                    height="32"
                  />
                  <span className="text-xl">
                    Quy trình bài bản, máy móc hiện đại, dung dịch chuyên dụng
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div>
            <Image
              src="/ve-chung-toi/heramo-an-tam.png"
              alt="Giá trị 3"
              width="120"
              height="120"
            />
            <div>
              <div className="text-4xl font-bold mb-3 mt-5">Tiện lợi</div>
              <div className="flex flex-col gap-4">
                <div className="flex items-start gap-5">
                  <Image
                    src="/ve-chung-toi/checked.svg"
                    alt="Checked"
                    width="32"
                    height="32"
                  />
                  <span className="text-xl">
                    Theo dõi đơn hàng mọi lúc mọi nơi
                  </span>
                </div>
                <div className="flex items-start gap-5">
                  <Image
                    src="/ve-chung-toi/checked.svg"
                    alt="Checked"
                    width="32"
                    height="32"
                  />
                  <span className="text-xl">
                    Thảnh thơi tận hưởng cuộc sống
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
