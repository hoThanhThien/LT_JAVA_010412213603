import Image from "next/image";

export default function GiaTri() {
  return (
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
                <span className="text-xl">Thảnh thơi tận hưởng cuộc sống</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
