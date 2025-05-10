import Image from "next/image";
import StaffComment from "@/components/staff-comment";
import Communication from "@/components/communication";
import Link from "next/link";
export default function TuyenDung() {
  return (
    <main className="relative">
      <div className="bg-[url('/home/bg-1.png')] bg-repeat-round h-[460px] flex justify-center items-center gap-20 px-4">
        <div>
          <Image
            src="/tuyen-dung/banner1.png"
            alt="banner"
            width="745"
            height="300"
          />
        </div>
        <div className="w-[20%] flex flex-col gap-5">
          <div>
            Hãy cùng HERAMO tạo ra những trải nghiệm tuyệt vời cho khách hàng và
            làm họ hạnh phúc!
          </div>
          <div className="flex items-center gap-2">
            <div className="px-[11px] py-1 inline-block rounded-[5px] text-2xl font-bold bg-white">
              4
            </div>
            <div className="text-xl font-bold">Việc làm đang chờ bạn</div>
          </div>
          <button className="bg-black text-white px-9 py-3 rounded-4xl font-bold cursor-pointer w-full">
            XEM NGAY
          </button>
        </div>
      </div>
      <div className="max-w-7xl mx-auto px-24 py-12">
        <div className="grid lg:grid-cols-4 gap-10">
          <div className="lg:col-span-1 lg:col-start-1 text-[28px] font-bold">
            Chúng tôi là ai?
          </div>
          <div className="lg:col-span-3 lg:col-start-2 text-gray-600 flex flex-col gap-5">
            <div>
              HERAMO - Startup tiên phong tại Việt Nam trong việc ứng dụng công
              nghệ 4.0 để cách mạng hóa trải nghiệm giặt ủi & vệ sinh chỉ với
              một chạm cho hàng triệu người.
            </div>
            <div>
              HERAMO - Startup tiên phong tại Việt Nam trong việc ứng dụng công
              nghệ 4.0 để cách mạng hóa trải nghiệm giặt ủi & vệ sinh chỉ với
              một chạm cho hàng triệu người. Thành lập cuối năm 2017 từ 2 nhà
              sáng lập, sau hơn 4 năm hoạt động, tập thể HERAMO đã có hơn 70
              thành viên - được chuyên môn hóa thành các phòng ban riêng biệt
              đảm nhiệm các vai trò khác nhau trong hành trình mang lại trải
              nghiệm vượt trội đến với khách hàng.
            </div>
            <div>
              HERAMO tự hào khi được vinh danh TOP 3 cuộc thi Startup Wheel 2021
              - cuộc thi khởi nghiệp hàng đầu Việt Nam - một sự công nhận từ
              cộng đồng khởi nghiệp và các chuyên gia về mô hình kinh doanh tiềm
              năng và những giá trị mang lại cho cộng đồng, xã hội.
            </div>
          </div>
        </div>
        <div className="grid lg:grid-cols-4 gap-10 my-24">
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
            <div>
              Trong 10 năm tới, ứng dụng HERAMO sẽ trở thành một trong những nền
              tảng công nghệ dịch vụ về đời sống (lifestyle) & nhà cửa (home
              service) lớn nhất Đông Nam Á - giúp đỡ hàng chục triệu khách hàng
              các công việc giặt ủi, vệ sinh không gian sống, chăm sóc gia đình
              để họ có thêm thật nhiều thời gian dành cho những người thân yêu,
              trải nghiệm hạnh phúc trọn vẹn và nâng cao chất lượng cuộc sống.
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
        <div className="flex justify-around mt-24">
          <div>
            <Image
              src="/tuyen-dung/about-us-1.jpg"
              alt="Why us"
              width="343"
              height="217"
              className="rounded-[20px] max-w-[343px]"
            />
          </div>
          <div>
            <Image
              src="/tuyen-dung/teampic-1.jpg"
              alt="Why us"
              width="343"
              height="217"
              className="rounded-[20px] max-w-[343px]"
            />
          </div>
          <div>
            <Image
              src="/tuyen-dung/teampic-3.jpg"
              alt="Why us"
              width="343"
              height="217"
              className="rounded-[20px] max-w-[343px]"
            />
          </div>
        </div>
        <div className="grid lg:grid-cols-2 max-w-7xl mx-auto">
          <div className="lg:col-span-1 lg:col-start-1">
            <StaffComment />
          </div>
          <div className="lg:col-span-1 lg:col-start-2 mt-24 mx-auto">
            <Image
              src="/tuyen-dung/about-us-3.jpg"
              alt="phone"
              width="460"
              height="560"
              className=""
            />
          </div>
        </div>
      </div>
      <div className="py-12">
        <Communication />
      </div>
      <div className="max-w-7xl mx-auto px-24 py-12">
        <div className="font-bold text-2xl text-center">
          Các vị trí tuyển dụng
        </div>
        <div className="flex flex-col gap-5 mt-12">
          <div className="flex justify-between items-center">
            <Link
              href="/tuyen-dung/business-analyst-cum-assistant-to-coo-part-time-intern"
              className="max-w-[70%] text-lg font-bold hover:underline cursor-pointer"
            >
              BUSINESS ANALYST cum ASSISTANT TO COO (PART-TIME / INTERN)
            </Link>
            <div className="max-w-[30%]">
              <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
                Xem chi tiết
                <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
              </button>
            </div>
          </div>
          <hr />
          <div className="flex justify-between items-center">
            <div className="max-w-[70%] text-lg font-bold hover:underline cursor-pointer">
              NHÂN VIÊN MARKETING OPERATION & OOH (INTERN / PART-TIME)
            </div>
            <div className="max-w-[30%]">
              <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
                Xem chi tiết
                <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
              </button>
            </div>
          </div>
          <hr />
          <div className="flex justify-between items-center">
            <div className="max-w-[70%] text-lg font-bold hover:underline cursor-pointer">
              TƯ VẤN BÁN HÀNG TẠI CỬA HÀNG (Part-time / Intern)
            </div>
            <div className="max-w-[30%]">
              <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
                Xem chi tiết
                <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
              </button>
            </div>
          </div>
          <hr />
          <div className="flex justify-between items-center">
            <div className="max-w-[70%] text-lg font-bold hover:underline cursor-pointer">
              NHÂN VIÊN TELESALES (Part-time / Intern)
            </div>
            <div className="max-w-[30%]">
              <button className="flex items-center mt-5 mx-auto gap-2 bg-main px-9 py-3 rounded-[28px] font-bold cursor-pointer">
                Xem chi tiết
                <Image src="/arrow.svg" alt="Arrow" width={20} height={20} />
              </button>
            </div>
          </div>
        </div>
      </div>
      <div className="bg-main flex flex-col justify-center items-center gap-5 py-12">
        <div className="text-center w-[30%]">
          Bạn còn chờ đợi gì nữa? Nhấn nút ứng tuyển để nộp hồ sơ hoặc liên hệ
          HERAMO để biết thêm chi tiết nhé.
        </div>
        <button className="bg-black text-white px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%]">
          ỨNG TUYỂN NGAY
        </button>
      </div>
    </main>
  );
}
