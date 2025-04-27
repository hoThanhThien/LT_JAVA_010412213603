"use client";

import Founder from "@/components/founder";
import Image from "next/image";
import Communication from "@/components/communication";
import Stores from "@/components/stores";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { CircleCheck, CircleX } from "lucide-react";
import Number from "@/components/number";
import FounderComment from "@/components/founder-comment";
import Services from "@/components/services";
import FAQ from "@/components/faq";
import Link from "next/link";
import ButtonMessage from "@/components/ui/buttonMessage";

export default function NhuongQuyen() {
  return (
    <main className="relative">
      <div className="bg-[url('/home/bg-1.png')] bg-no-repeat min-h-[960px] px-4 bg-[length:100%_1060px]">
        <div className="max-w-7xl mx-auto px-40 flex justify-center pt-15">
          <div className="w-[50%] text-left">
            <div className="text-[40px] font-bold">
              Nhượng quyền công nghệ giặt ủi vệ sinh cao cấp 4.0 có vốn đầu tư
              thấp nhất Việt Nam
            </div>
            <div className="text-2xl mt-3">
              Hệ thống cửa hàng nhượng quyền giặt ủi, giặt hấp, giặt sấy, giặt
              giày, vệ sinh cao cấp với 10 chi nhánh tại Hồ Chí Minh. Tiết kiệm
              90% thời gian vận hành. Hoàn vốn trong 12 tháng. Được cố vấn &
              đồng xây dựng bởi chuyên gia nhượng quyền Nguyễn Phi Vân.
            </div>
            <div className="mt-10">
              <ButtonMessage className="bg-black text-white px-9 py-3 rounded-4xl font-bold cursor-pointer w-[80%]" />
            </div>
          </div>
          <div className="w-[50%]">
            <Image
              src="/nhuong-quyen/phone-banner-desktop.png"
              alt="Banner"
              width="380"
              height="584"
              className="w-[380px] h-[584px] mx-auto"
            />
          </div>
        </div>
      </div>
      <div>
        <Founder />
      </div>
      <div className="mt-12">
        <Communication />
      </div>
      <div>
        <Stores />
      </div>
      <div className="max-w-7xl mx-auto px-4 w-full bg-[url('/nhuong-quyen/bg-desktop.png')] bg-no-repeat bg-cover">
        <div className="flex gap-12">
          <div>
            <Image
              src="/nhuong-quyen/bg-desktop-2.png"
              alt="bg"
              width="540"
              height="348"
            />
          </div>
          <div>
            <div className="text-4xl font-bold mt-2">
              Nhận ngay gói ưu đãi lên đến
            </div>
            <div className="text-4xl font-bold mt-2">30 TRIỆU ĐỒNG</div>
            <div className="text-lg mt-2">
              Khi ký kết thỏa thuận hợp tác trong tháng 11
            </div>
            <div className="flex gap-5 mt-8">
              <div className="bg-white rounded-2xl p-5 w-[50%] relative">
                <div className="text-2xl font-bold">Miễn phí</div>
                <div className="text-2xl font-bold">20 triệu</div>
                <div className="text-base mt-5 h-[70px]">
                  Gói 3 tháng tuyển dụng và đào tạo
                </div>
                <Image
                  src="/nhuong-quyen/hat.png"
                  alt="hat"
                  width="100"
                  height="100"
                  className="absolute top-[-32px] right-0"
                />
              </div>
              <div className="bg-white rounded-2xl p-5 w-[50%] relative">
                <div className="text-2xl font-bold">Miễn phí</div>
                <div className="text-2xl font-bold">20 triệu</div>
                <div className="text-base mt-5 h-[70px]">
                  Gói 3 tháng tuyển dụng và đào tạo
                </div>
                <Image
                  src="/nhuong-quyen/gift.png"
                  alt="gift"
                  width="100"
                  height="100"
                  className="absolute top-[-32px] right-0"
                />
              </div>
            </div>
          </div>
        </div>
        <div className="w-[50%] mx-auto mt-10 text-center">
          * Chỉ áp dụng gói marketing khai trương cho cửa hàng nhượng quyền tại
          các khu vực: Quận 5, Quận 6, Quận 8, Quận 11, Quận 12, Quận Tân Phú,
          Bình Tân.
        </div>
        <div className="flex justify-center items-center py-12">
          <ButtonMessage className="bg-black text-white hover:bg-transparent hover:text-black border border-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%] " />
        </div>
      </div>
      <div>
        <div className="text-4xl font-bold text-center my-12">
          Hình ảnh đội ngũ
        </div>
        <div className="flex gap-1">
          <Image
            src="/nhuong-quyen/hinh-anh-doi-ngu-4.jpg"
            alt="team"
            width="400"
            height="320"
            className="min-w-[22%] min-h-[320px]"
          />
          <Image
            src="/nhuong-quyen/hinh-anh-doi-ngu-1.jpg"
            alt="team"
            width="400"
            height="320"
            className="min-w-[22%] min-h-[320px]"
          />
          <Image
            src="/nhuong-quyen/hinh-anh-doi-ngu-2.jpg"
            alt="team"
            width="400"
            height="320"
            className="min-w-[22%] min-h-[320px]"
          />
          <Image
            src="/nhuong-quyen/hinh-anh-doi-ngu-3.jpg"
            alt="team"
            width="400"
            height="320"
            className="min-w-[22%] min-h-[320px]"
          />
        </div>
      </div>
      <div className="pt-24">
        <div className="text-4xl font-bold text-center mb-12">
          Vì sao nên lựa chọn mô hình nhượng quyền của HERAMO?
        </div>
        <div className="bg-[url('/nhuong-quyen/why-1.jpg')] bg-no-repeat bg-[length:100%_100%] min-h-[640px] flex flex-col justify-end items-center">
          <div className="min-w-[1031px] bg-white rounded-xl w-fit p-10 flex flex-col justify-center gap-5">
            <div className="flex items-center gap-5">
              <Image
                src="/nhuong-quyen/target_black.svg"
                alt="target"
                width="60"
                height="60"
              />
              <div className="text-2xl font-medium">
                Vận hành tinh gọn - chỉ cần 10% thời gian so với các mô hình
                khác
              </div>
            </div>
            <ul className="list-disc ml-4 space-y-2">
              <li>Chỉ tập trung bán hàng, không cần vận hành tiệm giặt ủi.</li>
              <li>
                Tất cả giao dịch, quá trình xử lý được thao tác trên app. Dễ
                dàng quản trị từ xa, theo dõi báo cáo kết quả kinh doanh mọi lúc
                mọi nơi
              </li>
              <li>Vận hành đơn giản chỉ cần 1 nhân viên tư vấn / ca</li>
            </ul>
          </div>
          <div className="w-full flex justify-center items-center py-12">
            <ButtonMessage className="bg-main text-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%]" />
          </div>
        </div>
      </div>
      <div className="bg-[url('/nhuong-quyen/why-2.jpg')] bg-no-repeat bg-[length:100%_100%] min-h-[640px] flex flex-col justify-end items-center">
        <div className="min-w-[1031px] bg-white rounded-xl w-fit p-10 flex flex-col justify-center gap-5">
          <div className="flex items-center gap-5">
            <Image
              src="/nhuong-quyen/config.svg"
              alt="target"
              width="60"
              height="60"
            />
            <div className="text-2xl font-medium">
              Hệ thống công nghệ độc quyền, quy trình hỗ trợ bài bản
            </div>
          </div>
          <ul className="list-disc ml-4 space-y-2">
            <li>Công nghệ chuyên biệt giúp tăng hiệu quả, giảm rủi ro.</li>
            <li>
              Nền tảng hỗ trợ do chuyên gia nhượng quyền hàng đầu xây dựng
            </li>
            <li>Đội ngũ hỗ trợ trẻ, chuyên nghiệp, phản ứng nhanh</li>
          </ul>
        </div>
        <div className="w-full flex justify-center items-center py-12">
          <ButtonMessage className="bg-main text-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%]" />
        </div>
      </div>
      <div className="bg-[url('/nhuong-quyen/why-3.jpg')] bg-no-repeat bg-[length:100%_100%] min-h-[640px] flex flex-col justify-end items-center">
        <div className="min-w-[1031px] bg-white rounded-xl w-fit p-10 flex flex-col justify-center gap-5">
          <div className="flex items-center gap-5">
            <Image
              src="/nhuong-quyen/money.svg"
              alt="target"
              width="60"
              height="60"
            />
            <div className="text-2xl font-medium">
              Mô hình nhượng quyền công nghệ có vốn đầu tư thấp nhất Việt Nam
            </div>
          </div>
          <ul className="list-disc ml-4 space-y-2">
            <li>
              Đầu tư nhượng quyền chỉ từ 100 triệu. Hoàn vốn trong 12 tháng
            </li>
            <li>
              Không cần đầu tư máy móc, có ngay 6 dịch vụ giặt ủi, vệ sinh cao
              cấp
            </li>
            <li>
              Doanh thu trung bình gấp 4 lần các cửa hàng giặt ủi truyền thống
            </li>
          </ul>
        </div>
        <div className="w-full flex justify-center items-center py-12">
          <ButtonMessage className="bg-main text-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%]" />
        </div>
      </div>
      <div className="bg-[url('/nhuong-quyen/why-4.jpg')] bg-no-repeat bg-[length:100%_100%] min-h-[640px] flex flex-col justify-end items-center">
        <div className="min-w-[1031px] bg-white rounded-xl w-fit p-5 flex items-center gap-5 mt-20">
          <Image
            src="/nhuong-quyen/eye.svg"
            alt="target"
            width="60"
            height="60"
          />
          <div className="text-2xl font-medium">
            So sánh với mô hình nhượng quyền khác
          </div>
        </div>
        <Table className="mt-20 font-bold text-lg max-w-[1031px] bg-white rounded-xl mx-auto [&_td,&_th]:whitespace-normal">
          <TableHeader className="text-base">
            <TableRow>
              <TableHead className="w-[45%] text-center"></TableHead>
              <TableHead className="w-[25%] text-center">
                Giặt ủi, vệ sinh cao cấp 4.0 Heramo
              </TableHead>
              <TableHead className="w-[10%] text-center">
                Kinh doanh ăn uống
              </TableHead>
              <TableHead className="w-[10%] text-center">
                Cửa hàng tiện lợi
              </TableHead>
              <TableHead className="w-[10%] text-center">
                Spa & làm đẹp
              </TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            <TableRow>
              <TableCell>
                Mô hình kinh doanh bền vững - nhu cầu không bao giờ cạn kiệt
              </TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
            </TableRow>

            <TableRow>
              <TableCell>
                Vận hành tinh gọn - không cần nhiều nhân lực, dễ vận hành từ xa
              </TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
            </TableRow>

            <TableRow>
              <TableCell>
                Hệ thống công nghệ theo dõi đơn hàng mọi lúc mọi nơi, giảm thiểu
                rủi ro vận hành
              </TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
            </TableRow>

            <TableRow>
              <TableCell>Đầu tư ban đầu thấp, tỷ suất sinh lời cao</TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
            </TableRow>

            <TableRow>
              <TableCell>
                Không lo tồn kho, không sợ kho bãi, hư hỏng, hao phí
              </TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleX className="text-red-500 mx-auto" />
              </TableCell>
              <TableCell>
                <CircleCheck className="text-green-500 mx-auto" />
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>

        <div className="w-full flex justify-center items-center py-12">
          <ButtonMessage className="bg-main text-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%]" />
        </div>
      </div>
      <div className="py-24">
        <div className="text-4xl font-bold text-center">
          Mô hình nhượng quyền
        </div>
        <div className="flex justify-center items-center gap-12 mt-10">
          <div className="w-[30%] border border-gray-200 rounded-4xl p-5 flex flex-col gap-5 justify-center items-center">
            <Image
              src="/nhuong-quyen/image_1.jpg"
              alt="tc1"
              width="400"
              height="300"
            />
            <div className="text-lg font-bold">Cửa hàng tiêu chuẩn</div>
            <div className="text-base text-center">
              <div>
                Diện tích tối thiểu từ 15 - 20m2. Mặt tiền tối thiểu 3.5m.
              </div>
              <div>Nhân viên vận hành: 1 người / ca làm việc</div>
              <div>
                Cửa hàng được đặt tại các con đường chính đông đúc; shophouse ở
                các khu căn hộ trung - cao cấp hoặc trung tâm thương mại
              </div>
            </div>
          </div>
          <div className="w-[30%] border border-gray-200 rounded-4xl p-5 flex flex-col gap-5 justify-center items-center">
            <Image
              src="/nhuong-quyen/image_1.jpg"
              alt="tc1"
              width="400"
              height="300"
            />
            <div className="text-lg font-bold">Cửa hàng tiêu chuẩn</div>
            <div className="text-base text-center">
              <div>
                Diện tích tối thiểu từ 15 - 20m2. Mặt tiền tối thiểu 3.5m.
              </div>
              <div>Nhân viên vận hành: 1 người / ca làm việc</div>
              <div>
                Cửa hàng được đặt tại các con đường chính đông đúc; shophouse ở
                các khu căn hộ trung - cao cấp hoặc trung tâm thương mại
              </div>
            </div>
          </div>
        </div>
        <div className="w-full flex justify-center items-center pt-12">
          <ButtonMessage className="bg-main text-black px-9 py-3 rounded-4xl font-bold cursor-pointer w-[30%]" />
        </div>
      </div>
      <div>
        <Number />
      </div>
      <div className="grid lg:grid-cols-2 max-w-7xl mx-auto">
        <div className="lg:col-span-1 lg:col-start-1">
          <FounderComment />
        </div>
        <div className="lg:col-span-1 lg:col-start-2">
          <Image
            src="/nhuong-quyen/double-phone.png"
            alt="phone"
            width="568"
            height="639"
          />
        </div>
      </div>
      <div className="pt-20">
        <Services />
      </div>
      <div>
        <FAQ />
      </div>
      <div className="bg-main">
        <div className="grid grid-cols-2">
          <div className="flex flex-col justify-center items-end">
            <div className="text-[26px] font-bold mb-10 text-center w-[60%]">
              Tiết kiệm 90% thời gian vận hành với lợi nhuận hàng năm đến 200
              triệu
            </div>
            <Link
              target="_blank"
              href="https://docs.google.com/forms/d/1Sjlp5MS6UEFGYifjnZJJ0qsUPSsiSwgke3xtUxMgB-c"
            >
              <div className="flex justify-center items-center">
                <ButtonMessage className="bg-black text-white px-9 py-3 rounded-4xl font-bold cursor-pointer" />
              </div>
            </Link>
          </div>
          <div className="flex justify-center items-center">
            <Image
              src="/nhuong-quyen/girl-bg-3.png"
              alt="job"
              width="262"
              height="250"
            />
          </div>
        </div>
      </div>
    </main>
  );
}
