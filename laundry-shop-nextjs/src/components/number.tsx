"use client";

import Image from "next/image";

export default function Number() {
  return (
    <div className="bg-gray-200 py-24 px-[300px]">
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
            <div className="text-gray-600 text-sm mt-1">Số năm kinh nghiệm</div>
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
  );
}
