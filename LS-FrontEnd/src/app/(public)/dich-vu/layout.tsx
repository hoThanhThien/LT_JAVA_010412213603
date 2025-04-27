"use client";

import Comment from "@/components/comment";
import Communication from "@/components/communication";
import DownloadApp from "@/components/dowload-app";
import GiaTri from "@/components/gia-tri";

import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";
import Image from "next/image";
import { usePathname } from "next/navigation";

const listGiatSayFAQ = [
  {
    id: 1,
    question: "Giá giặt sấy bao nhiêu 1kg? Giặt lố số kg sẽ như thế nào?",
    answer:
      "Dịch vụ giặt sấy tại HERAMO không tính theo kg mà tính theo mỗi 6kg (tròn một máy giặt, sấy). Bởi chúng tôi luôn giặt & sấy đồ của từng khách hàng theo máy riêng để đảm bảo an toàn vệ sinh & hạn chế tình trạng nhầm lẫn đồ. Vì vậy, ngay cả khi bạn chỉ giặt 1 món đồ, HERAMO vẫn sẽ giặt, sấy riêng với mức giá không đổi.",
  },
  {
    id: 2,
    question: "Giặt sấy mất bao lâu thì nhận được?",
    answer:
      "Thời gian giặt sấy tiêu chuẩn tại HERAMO dao động từ 2 - 3 ngày. Đây là thời gian cần thiết để kiểm tra phân loại, xử lý, sấy khô đồ. Nếu bạn muốn sử dụng dịch vụ giặt sấy lấy liền, liên hệ ngay đến Hotline 19003286 để được tư vấn và hỗ trợ nhanh nhất.",
  },
  {
    id: 3,
    question: "Gói giặt sấy bên HERAMO có bao gồm dịch vụ ủi không?",
    answer:
      "Gói giặt sấy chưa bao gồm dịch vụ ủi. Khách hàng có nhu cầu sử dụng dịch vụ ủi, xếp liên hệ ngay đến Hotline 19003286 để được tư vấn và hỗ trợ nhanh nhất.",
  },
  {
    id: 4,
    question: "HERAMO có dịch vụ giặt ủi tận nhà không? Phí ship như thế nào?",
    answer:
      "HERAMO sẵn sàng phục vụ bạn từ 09:00 - 21:00 hầu hết các khu vực ở Thành phố Hồ Chí Minh. Phí giao nhận tận nhà chưa đến 50% phí các đơn vị vận chuyển khác.",
  },
  {
    id: 5,
    question: "Khi shipper giao nhận có biên lai không?",
    answer:
      "Đồ của bạn sẽ được giao nhận bởi đội ngũ shipper được đào tạo bài bản bởi HERAMO, luôn mặc đồng phục, thùng xe có logo công ty và gửi biên nhận điện tử được bảo mật bằng OTP.",
  },
  {
    id: 6,
    question: "Trả tiền trước hay sau khi nhận đồ? Tiền mặt hay chuyển khoản?",
    answer:
      "Bạn có thể thanh toán khi shipper giao lại đồ đã được vệ sinh sạch thơm. Hiện tại, HERAMO chấp nhận các hình thức thanh toán tiền mặt, chuyển khoản qua ngân hàng, chuyển khoản qua MOMO.",
  },
];

const listGiatHapSayKhoFAQ = [
  {
    id: 1,
    question: "Giặt khô là gì, quy trình giặt khô của HERAMO như thế nào?",
    answer:
      "Giặt khô là phương pháp giặt quần áo không cần dùng nước. Thay vào đó, sử dụng các hợp chất hữu cơ là dung môi giặt để làm sạch vết bẩn giúp quần áo không bị nhăn nhúm, mất nếp, co rút hoặc phai màu. ",
  },
  {
    id: 2,
    question: "Giặt khô là gì, quy trình giặt khô của HERAMO như thế nào?",
    answer: "Giá giặt hấp áo vest tại HERAMO chỉ từ 80K.",
  },
  {
    id: 3,
    question: "Giặt khô là gì, quy trình giặt khô của HERAMO như thế nào?",
    answer:
      "Thời gian giặt khô tiêu chuẩn tại HERAMO dao động từ 2 - 3 ngày. Đây là thời gian cần thiết để kiểm tra phân loại, xử lý với dung môi giặt khô chuyên dụng, ủi pressing và thổi phom. Nếu bạn cần đồ trong thời gian gấp, liên hệ ngay đến Hotline 19003286 để được tư vấn và hỗ trợ nhanh nhất.",
  },
  {
    id: 4,
    question:
      "HERAMO có dịch vụ giặt hấp giao tận nhà không? Phí ship như thế nào?",
    answer:
      "HERAMO sẵn sàng phục vụ bạn từ 09:00 - 21:00 hầu hết các khu vực ở Thành phố Hồ Chí Minh. Phí giao nhận tận nhà chưa đến 50% phí các đơn vị vận chuyển khác.",
  },
  {
    id: 5,
    question: "Khi shipper giao nhận có biên lai không?",
    answer:
      "Đồ của bạn sẽ được giao nhận bởi đội ngũ shipper được đào tạo bài bản bởi HERAMO, luôn mặc đồng phục, thùng xe có logo công ty và gửi biên nhận điện tử được bảo mật bằng OTP.",
  },
];

const listVeSinhGiayFAQ = [
  {
    id: 1,
    question: "Giá giặt giày là bao nhiêu?",
    answer: "Giá vệ sinh giày toàn diện tại HERAMO chỉ từ 139K",
  },
  {
    id: 2,
    question: "Giá giặt giày là bao nhiêu?",
    answer:
      "Thời gian vệ sinh giày tiêu chuẩn tại HERAMO dao động từ 2 - 3 ngày. Đây là thời gian cần thiết để làm sạch các vết bẩn bên trong, bên ngoài giày; hấp khô & khử mùi bằng tia UV.",
  },
  {
    id: 3,
    question:
      "HERAMO có dịch vụ giặt giày giao tận nhà không? Phí ship như thế nào?",
    answer:
      "HERAMO sẵn sàng phục vụ bạn từ 09:00 - 21:00 hầu hết các khu vực ở Thành phố Hồ Chí Minh. Phí giao nhận tận nhà chưa đến 50% phí các đơn vị vận chuyển khác.",
  },
  {
    id: 4,
    question: "Khi shipper giao nhận có biên lai không?",
    answer:
      "Đồ của bạn sẽ được giao nhận bởi đội ngũ shipper được đào tạo bài bản bởi HERAMO, luôn mặc đồng phục, thùng xe có logo công ty và gửi biên nhận điện tử được bảo mật bằng OTP",
  },
  {
    id: 5,
    question: "Trả tiền trước hay sau khi nhận đồ? Tiền mặt hay chuyển khoản?",
    answer:
      "Hiện tại, HERAMO chấp nhận các hình thức thanh toán tiền mặt, chuyển khoản qua ngân hàng, chuyển khoản qua MOMO.",
  },
];

const listVeSinhSofaFAQ = [
  {
    id: 1,
    question: "Heramo sử dụng dung dịch vệ sinh gì? Có an toàn không?",
    answer:
      "Đối với dịch vụ giặt sofa, nệm, rèm, thảm HERAMO chỉ sử dụng dung dịch vệ sinh nhập khẩu, chuyên dụng Goodmaid Pro của Malaysia. Dung dịch này được chứng minh có độ an toàn tuyệt đối với người sử dụng & thân thiện với chất liệu vải.",
  },
  {
    id: 2,
    question:
      "Heramo có cam kết về chất lượng không? Nếu vệ sinh không sạch thì làm như thế nào?",
    answer:
      "Chúng tôi luôn cam kết sự hài lòng tối đa cho khách hàng và nỗ lực đem đến chất lượng dịch vụ tốt nhất. Trong trường hợp khách hàng chưa hài lòng, HERAMO luôn sẵn lòng xem xét, xử lý lại cho khách hàng để mang đến trải nghiệm trọn vẹn nhất. Tuy nhiên, vì đặc tính của vết bẩn (ví dụ các vết cà phê, rượu vang đã thấm sâu vào sợi vải, vết máu v.v...) và đặc tính chất liệu (một số chất liệu không thể vệ sinh quá mạnh tay có thể dẫn đến hư hỏng), HERAMO có thể sẽ không làm sạch hoàn toàn được. HERAMO rất mong khách hàng thấu hiểu & thông cảm trong những trường hợp như thế này.",
  },
  {
    id: 3,
    question: "Thời gian xử lý tại nhà khoảng bao lâu?",
    answer:
      "Tùy thuộc vào món đồ cần vệ sinh & các đặc tính chất liệu, thời gian xử lý tại nhà sẽ dao động từ: 45 - 60 phút đối với dịch vụ giặt nệm , 30 - 60 phút đối với dịch vụ giặt sofa, giặt thảm",
  },
  {
    id: 4,
    question: "Vệ sinh xong đồ có mùi ẩm không? Có gây mốc không?",
    answer:
      "HERAMO chỉ sử dụng dung dịch vệ sinh dễ bay hơi, kết hợp với máy thổi công suất lớn. Vì thế, chúng tôi cam kết sofa, nệm, thảm sẽ khô thoáng, không có hiện tượng ẩm mốc. Bạn có thể sử dụng ngay sau khi vệ sinh từ 3 - 5 tiếng.",
  },
  {
    id: 5,
    question: "Trường hợp hư hỏng, thất đồ trong nhà thì như thế nào?",
    answer:
      "Trong trường hợp cực kỳ hi hữu, nếu có xảy ra đổ bể, thất lạc đồ, bạn sẽ nhận được phản hồi & hướng dẫn, hỗ trợ tức thì từ phía HERAMO. Lưu ý, sau khi nhân viên đã rời đi, HERAMO có quyền từ chối giải quyết các phản hồi liên quan đến mất mác đồ đạc.",
  },
];

const listVeSinhMayLanhFAQ = [
  {
    id: 1,
    question: "Tại sao bạn cần dịch vụ vệ sinh máy lạnh?",
    answer:
      "Bạn cần dịch vụ vệ sinh máy lạnh để máy hoạt động hiệu quả, tiết kiệm điện và kéo dài tuổi thọ.",
  },
  {
    id: 2,
    question: "Thời gian xử lý tại nhà khoảng bao lâu?",
    answer:
      "Tùy thuộc vào dòng máy, vị trí & tình trạng máy, thời gian vệ sinh có thể dao động từ 45 - 60 phút.",
  },
  {
    id: 3,
    question:
      "HERAMO có cam kết về chất lượng hay không? Nếu vệ sinh không sạch thì làm như thế nào?",
    answer:
      "Chúng tôi luôn cam kết sự hài lòng tối đa cho khách hàng và nỗ lực đem tới chất lượng dịch vụ tốt nhất. Trong trường hợp khách hàng chưa hài lòng, HERAMO luôn sẵn lòng xem xét, xử lý lại cho khách hàng để mang đến trải nghiệm trọn vẹn nhất.",
  },
  {
    id: 4,
    question: "Khi nào cần bơm ga máy lạnh?",
    answer:
      "Bạn cần bơm ga máy lạnh khi máy lạnh yếu lạnh, chạy lâu không mát hoặc báo lỗi thiếu ga.",
  },
  {
    id: 5,
    question: "Trường hợp hư hỏng, đổ bể đồ trong nhà thì sao?",
    answer:
      "Trước khi vào nhà, nhân viên Kiểm soát Chất Lượng của HERAMO sẽ nhắn bạn thu dọn, bảo quản tiền bạc, tư trang và đồ đạc giá trị cao để hạn chế tối đa các vấn đề phát sinh. Sau khi hoàn tất quy trình vệ sinh & chuẩn bị ký bên bản nghiệm thu, nhân viên Kiểm soát Chất lượng sẽ nhắc nhở bạn kiểm tra lại đồ đạc, tư trang một lần nữa, đảm bảo không có mất mát nào xảy xa.",
  },
  {
    id: 6,
    question:
      "Nhân viên Kiểm Soát Chất Lượng của HERAMO là ai? Thực hiện nhiệm vụ gì?",
    answer:
      "Nhân viên Kiểm soát Chất lượng (Quality Control) được sàng lọc, tuyển dụng & đào tạo bài bản bởi HERAMO. Với kiến thức chuyên sâu, tác phong chuyên nghiệp, nhân viên Kiểm soát Chất lượng thay mặt HERAMO giám sát quá trình thi công của các đối tác kỹ thuật - đảm bảo chất lượng đạt mức tốt nhất & tránh các sự cố, vấn đề phát sinh nằm ngoài phạm vi thi công.",
  },
];

const defaultFAQ: any[] = [];

export default function layout({ children }: { children: React.ReactNode }) {
  const path = usePathname() || "";

  let listFAQ: any[] = defaultFAQ;

  if (path.startsWith("/dich-vu/giat-say")) {
    listFAQ = listGiatSayFAQ;
  } else if (path.startsWith("/dich-vu/giat-hap-giat-kho")) {
    listFAQ = listGiatHapSayKhoFAQ;
  } else if (path.startsWith("/dich-vu/ve-sinh-giay")) {
    listFAQ = listVeSinhGiayFAQ;
  } else if (path.startsWith("/dich-vu/ve-sinh-giat-sofa-nem-rem-tham")) {
    listFAQ = listVeSinhSofaFAQ;
  } else if (path.startsWith("/dich-vu/ve-sinh-may-lanh")) {
    listFAQ = listVeSinhMayLanhFAQ;
  }
  return (
    <div>
      <div>{children}</div>
      <div>
        <div className="max-w-7xl mx-auto px-4">
          <div className="text-4xl font-bold text-center">
            Dịch vụ giao nhận
          </div>
          <div className="text-base text-center text-gray-400 mt-4 mb-15 w-[50%] mx-auto">
            Dịch vụ giao nhận tận nơi của HERAMO mang đến trải nghiệm nhanh
            chóng và an toàn, với hơn 90% khách hàng hài lòng. Đội ngũ shipper
            chuyên nghiệp được đào tạo bài bản, luôn mặc đồng phục và cung cấp
            biên nhận điện tử khi giao hàng.
          </div>
          <div className="grid grid-cols-2">
            <div>
              <div className="text-xl font-bold">
                Phí giao nhận tiết kiệm hơn 50% so với các công ty vận chuyển*:
              </div>
              <div className="w-[65%] flex flex-col gap-4 my-5">
                <div className="flex items-start gap-5">
                  <Image
                    src="/ve-chung-toi/checked.svg"
                    alt="Checked"
                    width="32"
                    height="32"
                  />
                  <span className="text-xl">
                    15.000/chiều - Quận 1, 3, 4 ,5 ,10, Phú Nhuận, Gò Vấp, Tân
                    Bình, Bình Thạnh
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
                    20.000/chiều - Quận 2, 6, 7, 8, 11, Tân Phú
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
                    25.000/chiều - Quận 12, 9, Bình Tân, Thủ Đức, Bình Chánh,
                    Nhà Bè
                  </span>
                </div>
              </div>
              <div className="text-xl font-bold">
                Đặc biệt, miễn phí giao nhận 2 chiều khi đặt đơn từ 1.000.000.
              </div>
              <div className="text-sm text-gray-400 mt-4">
                (*) Áp dụng cho đơn hàng từ 350.000. Đối với đơn dưới 350.000,
                phí giao nhận sẽ có sự thay đổi.
              </div>
            </div>
            <div>
              <Image
                src="/dich-vu/shipper-info.png"
                alt="Shipper Info"
                width="458"
                height="384"
              />
            </div>
          </div>
        </div>
        <GiaTri />
        <Communication />
        <Comment />
        <DownloadApp />
        {listFAQ.length > 0 && (
          <div className="max-w-7xl mx-auto px-4 py-12">
            <div className="text-4xl font-bold text-center">FAQ</div>
            <Accordion type="single" collapsible>
              {listFAQ.map((item) => {
                return (
                  <AccordionItem key={item.id} value={`item-${item.id}`}>
                    <AccordionTrigger className="text-xl font-bold cursor-pointer">
                      {item.question}
                    </AccordionTrigger>
                    <AccordionContent className="text-base">
                      {item.answer}
                    </AccordionContent>
                  </AccordionItem>
                );
              })}
            </Accordion>
          </div>
        )}
      </div>
    </div>
  );
}
