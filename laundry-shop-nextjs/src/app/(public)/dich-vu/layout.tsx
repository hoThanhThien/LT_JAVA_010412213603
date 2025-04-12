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
import { usePathname } from "next/navigation";

const listGiatHapFAQ = [
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

const defaultFAQ: any[] = [];

export default function layout({ children }: { children: React.ReactNode }) {
  const path = usePathname() || "";

  let listFAQ: any[] = defaultFAQ;

  if (path.startsWith("/dich-vu/giat-hap")) {
    listFAQ = listGiatHapFAQ;
  }
  return (
    <div>
      <div>{children}</div>
      <div>
        <GiaTri />
        <Communication />
        <Comment />
        <DownloadApp />
        {listFAQ.length > 0 && (
          <div className="max-w-7xl mx-auto px-4">
            <div className="text-4xl font-bold text-center">FAQ</div>
            <Accordion type="single" collapsible>
              {listFAQ.map((item) => {
                return (
                  <AccordionItem key={item.id} value={`item-${item.id}`}>
                    <AccordionTrigger className="text-xl font-bold">
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
