"use client";

import { useRef, useState, useEffect } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { ProTable } from "@ant-design/pro-components";
import shopApiRequests from "@/apiRequests/shop";
import { ShopType } from "@/schemaValidations/store.schema";
import { formatDate } from "@/lib/utils";

export default function page() {
  const [mounted, setMounted] = useState(false);
  const actionRef = useRef<ActionType>(null);
  const [meta, setMeta] = useState({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0,
  });
  const [currentPage, setCurrentPage] = useState(1); // Bắt đầu từ 1 cho UI
  const [pageSize, setPageSize] = useState(10);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  // Hàm fetch data riêng để có thể gọi lại khi cần
  const fetchData = async (page: number, size: number) => {
    setLoading(true);
    try {
      // API thường yêu cầu page bắt đầu từ 0
      const apiPage = page - 1;
      const query = await shopApiRequests.admin(apiPage, size);
      const res = query.payload;

      if (res.data) {
        setMeta(res.data.meta);
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

  // Khi component mount, trigger fetch dữ liệu ban đầu
  useEffect(() => {
    if (mounted && actionRef.current) {
      actionRef.current.reload();
    }
  }, [mounted]);

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
        request={async () => {
          return await fetchData(currentPage, pageSize);
        }}
        rowKey="id"
        pagination={{
          current: currentPage,
          pageSize: pageSize,
          total: meta.totalElements,
          onChange: (page, size) => {
            setCurrentPage(page);
            setPageSize(size);
          },
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
