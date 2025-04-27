"use client";

import shopApiRequests from "@/apiRequests/shop";
import { ShopType } from "@/schemaValidations/store.schema";
import React, { useEffect, useState } from "react";

import { Button, Descriptions } from "antd";
import type { DescriptionsProps } from "antd";

export default function page() {
  const [shop, setShop] = useState<{ data: ShopType; message: string } | null>(
    null
  );
  const getShop = async () => {
    const result = await shopApiRequests.storeOwner();
    setShop(result.payload);
  };

  useEffect(() => {
    getShop();
    console.log(shop);
  }, []);

  const items: DescriptionsProps["items"] = [
    {
      key: "1",
      label: "Tên cửa hàng",
      children: `${shop?.data.name}`,
    },
    {
      key: "2",
      label: "Giờ mở cửa",
      children: `${shop?.data.openingHours}`,
    },
    {
      key: "3",
      label: "Địa chỉ",
      children: `${shop?.data.address}`,
    },
    {
      key: "4",
      label: "Mô tả",
      children: `${shop?.data.description}`,
    },
  ];

  return (
    <div className="bg-white p-5 rounded-3xl">
      <Descriptions title="Cửa hàng" items={items} />
      <Button type="primary" className="mt-5">
        Cập nhập thông tin
      </Button>
    </div>
  );
}
