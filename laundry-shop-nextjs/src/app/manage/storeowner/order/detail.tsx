"use client";

import { formatDate } from "@/lib/utils";
import { OrderListType } from "@/schemaValidations/order.schema";

import { Descriptions, Drawer, Image, Tag } from "antd";
import { useEffect, useState } from "react";

export const Detail = (props: {
  openViewDetail: boolean;
  setOpenViewDetail: (v: boolean) => void;
  dataViewDetail: OrderListType | null;
  setDataViewDetail: (v: OrderListType | null) => void;
}) => {
  const {
    openViewDetail,
    setOpenViewDetail,
    dataViewDetail,
    setDataViewDetail,
  } = props;

  const [formattedImage, setFormattedImage] = useState<string>("");

  useEffect(() => {
    if (dataViewDetail?.imgProduct) {
      // Kiểm tra xem imgProduct đã có tiền tố data:image chưa
      if (dataViewDetail?.imgProduct.startsWith("data:image")) {
        setFormattedImage(dataViewDetail?.imgProduct);
      } else {
        // Giả định rằng imgProduct là chuỗi base64 không có tiền tố
        // Cần xác định kiểu MIME (ví dụ: PNG, JPEG) - ở đây giả định là PNG
        setFormattedImage(
          `data:image/png;base64,${dataViewDetail?.imgProduct}`
        );
      }
    }
  }, [dataViewDetail?.imgProduct]);

  return (
    <>
      <Drawer
        title="Xem chi tiết"
        width={"50vw"}
        onClose={() => {
          setOpenViewDetail(false);
          setDataViewDetail(null);
        }}
        open={openViewDetail}
        destroyOnClose={true}
      >
        <Descriptions title="Xem chi tiết" bordered column={1}>
          <Descriptions.Item label="Tài khoản">
            {dataViewDetail?.phone}
          </Descriptions.Item>

          <Descriptions.Item label="Họ và tên đăng ký">
            {dataViewDetail?.username}
          </Descriptions.Item>

          <Descriptions.Item label="Ngày tạo đơn">
            {formatDate(dataViewDetail?.createdAt)}
          </Descriptions.Item>

          <Descriptions.Item label="Dịch vụ">
            {dataViewDetail?.serviceCategoryName} -{" "}
            {dataViewDetail?.serviceName}
          </Descriptions.Item>

          <Descriptions.Item label="Trạng thái">
            <Tag color="gold">{dataViewDetail?.orderStatus}</Tag>
          </Descriptions.Item>

          <Descriptions.Item label="Ghi chú">
            {dataViewDetail?.instructions}
          </Descriptions.Item>
          <Descriptions.Item label="Hình ảnh">
            {formattedImage !== "" ? (
              <Image width={200} src={formattedImage} alt="image" />
            ) : (
              <span>Không có hình ảnh</span>
            )}
          </Descriptions.Item>
        </Descriptions>
      </Drawer>
    </>
  );
};
