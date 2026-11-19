"use client";

import { useRef, useState } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { ProTable } from "@ant-design/pro-components";
import shopApiRequests from "@/apiRequests/shop";
import { ShopType } from "@/schemaValidations/store.schema";
import { formatDate } from "@/lib/utils";
import { useHydrated } from "@/hooks/use-hydrated";

export default function AdminStoresPage() {
  const mounted = useHydrated();
  const actionRef = useRef<ActionType>(null);
  const [loading, setLoading] = useState(false);

  // Hàm fetch data riêng để có thể gọi lại khi cần
  const fetchData = async (page: number, size: number) => {
    setLoading(true);
    try {
      // API thường yêu cầu page bắt đầu từ 0
      const apiPage = page - 1;
      const query = await shopApiRequests.admin(apiPage, size);
      const res = query.payload;

      if (res.data) {
        return {
          data: res.data.result || [],
          success: true,
          total: res.data.meta.totalElements || 0,
        };
      }
      return { data: [], success: false, total: 0 };
    } catch (error) {
      console.error("Failed to fetch orders:", error);
      return { data: [], success: false, total: 0 };
    } finally {
      setLoading(false);
    }
  };

  if (!mounted) {
    return null;
  }

  const columns: ProColumns<ShopType>[] = [
    {
      dataIndex: "index",
      valueType: "indexBorder",
      width: 48,
    },
    {
      title: "Tên",
      dataIndex: "name",
      align: "center",
    },
    {
      title: "Giờ mở cửa",
      dataIndex: "openingHours",
      align: "center",
    },
    {
      title: "Địa chỉ",
      dataIndex: "address",
      align: "center",
    },
    {
      title: "Đánh giá",
      dataIndex: "averageRating",
      align: "center",
    },
    {
      title: "Cửa hàng trưởng",
      align: "center",
      render(_, entity) {
        return <div>{entity.storeOwner.username}</div>;
      },
    },
    {
      title: "Tạo",
      align: "center",
      render(_, entity) {
        return <div>{formatDate(entity.createdAt)}</div>;
      },
    },
  ];

  return (
    <>
      <ProTable<ShopType>
        columns={columns}
        actionRef={actionRef}
        cardBordered
        search={false}
        request={async (params) => {
          return await fetchData(params.current ?? 1, params.pageSize ?? 10);
        }}
        rowKey="id"
        pagination={{
          defaultPageSize: 10,
          showSizeChanger: true,
          showTotal: (total, range) => (
            <div>
              {range[0]}-{range[1]} trên {total} rows
            </div>
          ),
        }}
        headerTitle="Quản lý cửa hàng"
        loading={loading}
      />
    </>
  );
}
