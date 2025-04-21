"use client";

import { useRef, useState, useEffect } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { ProTable } from "@ant-design/pro-components";
import { EllipsisIcon } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { OrderListType } from "@/schemaValidations/order.schema";
import orderApiRequests from "@/apiRequests/order";

export default function AdminOrder() {
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
      const query = await orderApiRequests.adminOrder(apiPage, size);
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

  const columns: ProColumns<OrderListType>[] = [
    {
      dataIndex: "index",
      valueType: "indexBorder",
      width: 48,
    },
    {
      title: "Mã đơn",
      dataIndex: "id",
      copyable: true,
    },
    {
      title: "Họ tên",
      // dataIndex: "serviceName",
    },
    {
      title: "Số điện thoại",
      // dataIndex: "serviceName",
    },
    {
      title: "Dịch vụ",
      dataIndex: "serviceCategoryName",
    },
    {
      title: "Cửa hàng",
      dataIndex: "laundryShopName",
    },
    {
      title: "Trạng thái",
      dataIndex: "orderStatus",
    },
    {
      title: "Tổng tiền",
      dataIndex: "totalAmount",
    },
    {
      hideInSearch: true,
      align: "center",
      render() {
        return (
          <>
            <DropdownMenu modal={false}>
              <DropdownMenuTrigger asChild>
                <EllipsisIcon className="cursor-pointer" />
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuItem className="cursor-pointer">
                  Xem chi tiết
                </DropdownMenuItem>
                <DropdownMenuItem className="cursor-pointer">
                  Xoá đơn hàng
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </>
        );
      },
    },
  ];

  return (
    <>
      <ProTable<OrderListType>
        columns={columns}
        actionRef={actionRef}
        cardBordered
        search={{
          searchText: "Tìm kiếm",
          resetText: "Làm mới",
          collapseRender: (collapsed) =>
            collapsed ? "Mở rộng +" : "Thu gọn -",
        }}
        request={async () => {
          return await fetchData(currentPage, pageSize);
        }}
        rowKey="id"
        pagination={{
          current: currentPage,
          pageSize: pageSize,
          total: meta.totalElements,
          showSizeChanger: true,
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
        headerTitle="Quản lý khách hàng"
        loading={loading}
      />
    </>
  );
}
