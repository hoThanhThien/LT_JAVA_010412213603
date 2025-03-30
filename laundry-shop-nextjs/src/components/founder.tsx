"use client";

import Image from "next/image";

const listFounder = [
  {
    id: 1,
    name: "Lê Phước Phúc",
    position_1: "CEO & Founder",
    position_2: "",
    image: "/nhuong-quyen/founder_1.png",
    description:
      "15 năm kinh nghiệm quản lý cấp cao trong lĩnh vực công nghệ, chuyển đổi số; phục vụ hàng chục triệu người dùng mỗi tháng",
  },
  {
    id: 2,
    name: "Nguyễn Phi Vân",
    position_1: "Founder & Chủ tịch Go Global Holdings",
    position_2: "Cố vấn & cổ đông Heramo",
    image: "/nhuong-quyen/founder_2.png",
    description:
      "Doanh nhân, nhà đầu tư thiên thần, tác giả với hơn 25 năm kinh nghiệm trong lĩnh vực bán lẻ, nhượng quyền, tăng tốc doanh nghiệp khởi nghiệp & doanh nghiệp vừa và nhỏ",
  },
  {
    id: 3,
    name: "Huỳnh Nhật Phương",
    position_1: "COO & Co-founder",
    position_2: "",
    image: "/nhuong-quyen/founder_3.png",
    description:
      "Thạc sĩ MBA với 13 năm quản lý trong ngành dịch vụ nhà hàng, khách sạn cao cấp, lấy khách hàng làm trọng tâm",
  },
  {
    id: 4,
    name: "Nguyễn Tuấn Quỳnh",
    position_1: "Co-founder Go Global Holdings",
    position_2: "Cố vấn & cổ đông Heramo",
    image: "/nhuong-quyen/founder_4.png",
    description:
      "Doanh nhân, nhà đầu tư thiên thần và giám đốc điều hành cấp cao với hơn 30 năm kinh nghiệm trong quản lý doanh nghiệp, M&A và IPO",
  },
];

export default function Founder() {
  return (
    <div className="mx-auto bg-gray-200 py-24 px-[300px]">
      <div className="text-center text-4xl font-bold">
        Đội ngũ sáng lập và cố vấn
      </div>
      <div>
        <div className="flex flex-wrap gap-10 justify-center items-center mt-10">
          {listFounder.map((item) => {
            return (
              <div
                key={item.id}
                className="flex items-start gap-5 max-w-[45%] p-5 border border-gray-200 rounded-4xl bg-white"
              >
                <Image
                  src={item.image}
                  alt="Founder"
                  width="72"
                  height="72"
                  className="w-[72px] h-[72px]"
                />
                <div>
                  <div className="text-lg font-bold">{item.name}</div>
                  <div className="text-sm text-gray-500 h-[60px]">
                    <div>{item.position_1}</div>
                    <div>{item.position_2}</div>
                  </div>
                  <div className="text-sm text-gray-500 h-[100px]">
                    {item.description}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
