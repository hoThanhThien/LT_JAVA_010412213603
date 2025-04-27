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
import { Tag } from "antd";
import { Detail } from "./detail";

export default function AdminOrder() {
  const [mounted, setMounted] = useState(false);
  const [openViewDetail, setOpenViewDetail] = useState<boolean>(false);
  const [dataViewDetail, setDataViewDetail] = useState<OrderListType | null>(
    null
  );

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
      const query = await orderApiRequests.storeOwnerOrder(apiPage, size);
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
      dataIndex: "username",
      align: "center",
    },
    {
      title: "Số điện thoại",
      align: "center",
      dataIndex: "phone",
    },
    {
      title: "Dịch vụ",
      align: "center",
      render(_, entity) {
        return (
          <div>
            {entity.serviceCategoryName} - {entity.serviceName}
          </div>
        );
      },
    },
    {
      title: "Trạng thái",

      align: "center",
      render: (_, entity) => (
        <Tag color={entity.orderStatus === "DONE" ? "green" : "yellow"}>
          {entity.orderStatus}
        </Tag>
      ),
    },
    {
      title: "Tổng tiền (VND)",
      dataIndex: "totalAmount",
      align: "center",
    },
    {
      hideInSearch: true,
      align: "center",
      render(_, entity) {
        return (
          <>
            <DropdownMenu modal={false}>
              <DropdownMenuTrigger asChild>
                <EllipsisIcon className="cursor-pointer" />
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuItem
                  onClick={() => {
                    setOpenViewDetail(true);
                    setDataViewDetail(entity);
                  }}
                  className="cursor-pointer"
                >
                  Xem chi tiết
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
        headerTitle="Quản lý đơn hàng"
        loading={loading}
      />
      <Detail
        openViewDetail={openViewDetail}
        setOpenViewDetail={setOpenViewDetail}
        dataViewDetail={dataViewDetail}
        setDataViewDetail={setDataViewDetail}
      />
    </>
  );
}
