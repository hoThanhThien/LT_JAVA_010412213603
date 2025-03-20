import Communication from "@/components/communication";
import Image from "next/image";
import Link from "next/link";

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
      <div className="bg-gray-100 py-24 px-[300px]">
        <div className="text-4xl font-medium text-center mb-20">
          Những con số biết nói
        </div>
        <div className="w-[900px]max-w-full flex flex-wrap justify-center gap-10">
          <div className="w-[30%] flex gap-2">
            <div>
              <Image
                src="/ve-chung-toi/kh.png"
                alt="kh"
                width="120"
                height="68"
                className="max-w-[120px]"
              />
            </div>
            <div>
              <div className="text-lg font-bold">136.000+</div>
              <div className="text-gray-600 text-sm mt-1">
                Số khách hàng tin yêu và lựa chọn
              </div>
            </div>
          </div>
          <div className="w-[30%] flex gap-2">
            <div>
              <Image
                src="/ve-chung-toi/dc.png"
                alt="kh"
                width="120"
                height="68"
                className="max-w-[120px]"
              />
            </div>
            <div>
              <div className="text-lg font-bold">420.000+</div>
              <div className="text-gray-600 text-sm mt-1">
                Tổng số món đồ đã từng vệ sinh chăm sóc
              </div>
            </div>
          </div>
          <div className="w-[30%] flex gap-2">
            <div>
              <Image
                src="/ve-chung-toi/smile.png"
                alt="kh"
                width="120"
                height="68"
                className="max-w-[120px]"
              />
            </div>
            <div>
              <div className="text-lg font-bold">99.7%</div>
              <div className="text-gray-600 text-sm mt-1">
                Tỉ lệ khách hàng hài lòng
              </div>
            </div>
          </div>
          <div className="w-[30%] flex gap-2">
            <div>
              <Image
                src="/ve-chung-toi/brand.png"
                alt="kh"
                width="120"
                height="68"
                className="max-w-[120px]"
              />
            </div>
            <div>
              <div className="text-lg font-bold">100+</div>
              <div className="text-gray-600 text-sm mt-1">
                Số việc làm đã tạo ra
              </div>
            </div>
          </div>
          <div className="w-[30%] flex gap-2">
            <div>
              <Image
                src="/ve-chung-toi/tick.png"
                alt="kh"
                width="120"
                height="68"
                className="max-w-[120px]"
              />
            </div>
            <div>
              <div className="text-lg font-bold">6+</div>
              <div className="text-gray-600 text-sm mt-1">
                Số năm kinh nghiệm
              </div>
            </div>
          </div>
          <div className="w-[30%] flex gap-2">
            <div>
              <Image
                src="/ve-chung-toi/km.png"
                alt="kh"
                width="120"
                height="68"
                className="max-w-[120px]"
              />
            </div>
            <div>
              <div className="text-lg font-bold">702.500km</div>
              <div className="text-gray-600 text-sm mt-1">
                Quãng đường shipper di chuyển để giao nhận
              </div>
            </div>
          </div>
        </div>
      </div>
      <div>
        <Communication />
      </div>
      <div className="max-w-7xl mx-auto px-24 pb-20">
        <div className="grid lg:grid-cols-4 gap-10 my-24">
          <div className="lg:col-span-1 lg:col-start-1 text-[28px] font-bold">
            Tầm nhìn
          </div>
          <div className="lg:col-span-3 lg:col-start-2 text-gray-600">
            <div>
              Trong 10 năm tới, HERAMO sẽ trở thành một trong những nền tảng
              công nghệ & nhượng quyền dịch vụ về đời sống (lifestyle) & nhà cửa
              (home service) lớn nhất Đông Nam Á - giúp đỡ hàng chục triệu khách
              hàng các công việc giặt ủi, vệ sinh không gian sống, chăm sóc gia
              đình để họ có thêm thật nhiều thời gian dành cho những người thân
              yêu, trải nghiệm hạnh phúc trọn vẹn và nâng cao chất lượng cuộc
              sống.
            </div>
          </div>
        </div>
        <div className="grid lg:grid-cols-4 gap-10">
          <div className="lg:col-span-1 lg:col-start-1 text-[28px] font-bold">
            Văn hóa công ty
          </div>
          <div className="lg:col-span-3 lg:col-start-2 text-gray-600 flex flex-col gap-5">
            <div>
              Ở HERAMO, tập thể nhân viên luôn đặt trải nghiệm khách hàng làm
              trung tâm của mọi quyết định & không ngừng nỗ lực phụng sự theo 6
              giá trị cốt lõi:
            </div>
            <div className="flex flex-col gap-5">
              <div>
                <span className="text-lg text-gray-600 font-medium">
                  H - HONEST (TRUNG THỰC):
                </span>{" "}
                Làm đúng những gì đã cam kết với khách hàng
              </div>
              <div>
                <span className="text-lg text-gray-600 font-medium">
                  E - EXCELLENT (XUẤT SẮC):
                </span>{" "}
                Chỉ chấp nhận chất lượng công việc mang đến dịch vụ xuất sắc
              </div>
              <div>
                <span className="text-lg text-gray-600 font-medium">
                  R - RESULT-ORIENTED (HƯỚNG ĐẾN KẾT QUẢ):
                </span>{" "}
                Sự hài lòng của khách hàng là thứ duy nhất đo lường thành quả
                công việc
              </div>
              <div>
                <span className="text-lg text-gray-600 font-medium">
                  A - ACCOUNTABLE (TRÁCH NHIỆM):
                </span>{" "}
                Chịu trách nhiệm cho mọi hành động và dịch vụ mang đến khách
                hàng
              </div>
              <div>
                <span className="text-lg text-gray-600 font-medium">
                  M - MOTIVATED (ĐỘNG LỰC):
                </span>{" "}
                Phục vụ khách hàng với nhiệt huyết, động lực cao nhất
              </div>
              <div>
                <span className="text-lg text-gray-600 font-medium">
                  O - OPEN (CỞI MỞ):
                </span>{" "}
                Sẵn sàng lắng nghe mọi ý kiến phản hồi với tinh thần cầu thị và
                không ngừng cải tiến để trở nên xuất sắc hơn
              </div>
            </div>
          </div>
        </div>
        <div className="flex justify-around mt-20">
          <div>
            <Image
              src="/ve-chung-toi/tam_nhin.jpg"
              alt="Why us"
              width="343"
              height="217"
              className="rounded-[20px] max-w-[343px]"
            />
          </div>
          <div>
            <Image
              src="/ve-chung-toi/cot_loi.jpg"
              alt="Why us"
              width="343"
              height="217"
              className="rounded-[20px] max-w-[343px]"
            />
          </div>
        </div>
      </div>
      <div className="bg-main h-fit">
        <div className="grid grid-cols-2">
          <div className="flex flex-col justify-center items-center">
            <div className="text-[26px] font-bold mb-10">
              Cơ hội nghề nghiệp
            </div>
            <Link href="/tuyen-dung">
              <button className="flex items-center gap-2 bg-transparent border-2 border-black px-9 py-2 rounded-[28px] font-bold cursor-pointer">
                Tới trang tuyển dụng
                <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
              </button>
            </Link>
          </div>
          <div className="flex justify-center items-center">
            <Image
              src="/ve-chung-toi/job.png"
              alt="job"
              width="246"
              height="295"
            />
          </div>
        </div>
      </div>
    </main>
  );
}
