"use client";

import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";

const listFAQ = [
  {
    id: 1,
    question:
      "Cửa hàng nhượng quyền giặt ủi cần bao nhiêu máy móc? Gồm những loại nào?",
    answer:
      "Với hơn 6 năm kinh nghiệm trong ngành giặt ủi, vệ sinh cao cấp HERAMO đã phát triển mô hình nhượng quyền giặt ủi, giặt hấp tinh gọn và hiệu quả. Nhà đầu tư không cần phải trang bị, mua sắm hệ thống máy móc, trang thiết bị đắt tiền, đồng thời không cần đau đầu về việc xây dựng quy trình vận hành, đào tạo nhân viên vệ sinh. HERAMO sẽ trực tiếp mang đồ khách từ cửa hàng về kho để phân loại, xử lý, kiểm soát chất lượng và vận chuyển lại đồ sạch cho cửa hàng mỗi ngày. Việc này sẽ giúp quy trình vận hành cực kỳ tinh gọn - giúp tiết kiệm công sức & tối đa hóa lợi nhuận, chỉ với 10% công sức vận hành có thể đem lại lợi nhuận trung bình 200 triệu/năm.",
  },
  {
    id: 2,
    question:
      "Chi phí đầu tư mô hình kinh doanh giặt ủi của HERAMO là bao nhiêu?",
    answer:
      "Mô hình kinh doanh giặt ủi của HERAMO sẽ giúp các nhà đầu tư tiết kiệm 90% công sức & chi phí. Dự toán chi phí đầu tư mô hình giặt ủi vệ sinh cao cấp 4.0 HERAMO khoảng 230.000.000 VNĐ. Chi phí này bao gồm:",
    list: [
      "Phí nhượng quyền thương hiệu 3 năm: 100.000.000 VNĐ.",
      "Phí setup tiệm giặt ủi tiêu chuẩn: 130.000.000 VNĐ.",
    ],
  },
  {
    id: 3,
    question: "Chi phí setup tiệm giặt ủi tiêu chuẩn gồm những gì?",
    answer:
      "Chi phí setup tiệm giặt ủi tiêu chuẩn (dự kiến 130.000.000 VNĐ đối với mặt bằng trung bình 15 - 20m2) bao gồm:",
    list: [
      "Cải tạo, sơn sửa, decor mặt bằng.",
      "Bảng hiệu quảng cáo, alu, bảng vẫy.",
      "Tivi cao cấp 32 inch, hệ thống 3 camera cloud lưu trữ 30 ngày.",
      "Nội thất, quầy kệ, bộ PCCC.",
      "Hàng hóa, dụng cụ, công cụ làm việc.",
    ],
  },
  {
    id: 4,
    question: "Hệ thống phần mềm công nghệ của HERAMO bao gồm những gì?",
    answer: "Hệ thống phần mềm công nghệ HERAMO bao gồm:",
    list: [
      "Ứng dụng khách hàng được thiết kế với giao diện trực quan, dễ sử dụng và có tính ổn định giúp khách hàng sử dụng dịch vụ giặt ủi, giặt sấy vệ sinh online hoàn toàn tiện lợi và an tâm. Hiện nay đã có hơn 44.000+ lượt tải trên cả 2 nền tảng Google Play và App Store.",
      "Tất cả nhân viên đều được làm việc dựa trên nền tảng công nghệ độc quyền được xây dựng chuyên biệt cho từng bộ phận nhằm tối ưu quá trình vận hành và nâng cao hiệu suất làm việc.",
      "Hệ thống web quản trị, dashboard báo cáo giúp nhà đầu tư có thể dễ dàng theo dõi các bảng chỉ số chi tiết về tình hình kinh doanh, vận hành của cửa hàng giặt ủi vệ sinh, từ đó phân tích và đưa ra các quyết định mang tính chiến lược trong kinh doanh.",
    ],
  },
];
export default function FAQ() {
  return (
    <>
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
                  <ul className="list-disc ml-10 space-y-2">
                    {item.list && (
                      <div className="text-base">
                        {item.list.map((item) => {
                          return <li key={item}>{item}</li>;
                        })}
                      </div>
                    )}
                  </ul>
                </AccordionContent>
              </AccordionItem>
            );
          })}
        </Accordion>
      </div>
    </>
  );
}
