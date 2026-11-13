"use client";

import shopApiRequests from "@/apiRequests/shop";
import { ShopType } from "@/schemaValidations/store.schema";
import React, { useEffect, useState } from "react";

import { Button, Card, Descriptions } from "antd";
import type { DescriptionsProps } from "antd";
import { EditStore } from "./editStore";
import { CreateStore } from "./createStore";

export default function StoreOwnerDashboardPage() {
  const [shop, setShop] = useState<ShopType | null>(null);
  const [openModalUpdate, setOpenModalUpdate] = useState<boolean>(false);

  const [openModalCreate, setOpenModalCreate] = useState<boolean>(false);

  const getShop = async () => {
    try {
      const result = await shopApiRequests.storeOwner();
      setShop(result.payload.data);
    } catch (error) {
      console.error("Failed to fetch shop:", error);
    }
  };

  useEffect(() => {
    let ignore = false;
    shopApiRequests
      .storeOwner()
      .then((result) => {
        if (!ignore) {
          setShop(result.payload.data);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch shop:", error);
      });
    return () => {
      ignore = true;
    };
  }, []);

  const items: DescriptionsProps["items"] = [
    {
      key: "2",
      label: "Giờ mở cửa",
      children: `${shop?.openingHours}`,
    },
    {
      key: "3",
      label: "Địa chỉ",
      children: `${shop?.address}`,
    },
    {
      key: "4",
      label: "Mô tả",
      children: `${shop?.description}`,
    },
  ];

  return (
    <Card title="Cửa hàng">
      {shop ? (
        <div>
          <Descriptions title={"Tên cửa hàng: " + shop.name} items={items} />
          <Button
            type="primary"
            className="mt-5"
            onClick={() => {
              setOpenModalUpdate(true);
            }}
          >
            Cập nhập thông tin
          </Button>
          <EditStore
            openModalUpdate={openModalUpdate}
            setOpenModalUpdate={setOpenModalUpdate}
            dataUpdate={shop}
            setDataUpdate={setShop}
            reloadData={getShop}
          />
        </div>
      ) : (
        <div>
          <Button
            type="primary"
            onClick={() => {
              setOpenModalCreate(true);
            }}
          >
            Tạo cửa hàng
          </Button>
          <CreateStore
            openModalCreate={openModalCreate}
            setOpenModalCreate={setOpenModalCreate}
            reloadData={getShop}
          />
        </div>
      )}
    </Card>
  );
}
