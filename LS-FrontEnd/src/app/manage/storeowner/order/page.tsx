"use client";

import { useRef, useState } from "react";
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
import { useHydrated } from "@/hooks/use-hydrated";
import { formatCurrency } from "@/lib/utils";

export default function StoreOwnerOrder() {
  const mounted = useHydrated();
  const [openViewDetail, setOpenViewDetail] = useState<boolean>(false);
  const [dataViewDetail, setDataViewDetail] = useState<OrderListType | null>(
    null
  );

  const actionRef = useRef<ActionType>(null);
  const [loading, setLoading] = useState(false);

  // Hàm fetch data riêng để có thể gọi lại khi cần
  const fetchData = async (page: number, size: number) => {
    setLoading(true);
    try {
      // API thường yêu cầu page bắt đầu từ 0
      const apiPage = page - 1;
      const query = await orderApiRequests.storeOwnerOrder(apiPage, size);
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
      render: (_, entity) => <span>{formatCurrency(entity.totalAmount)}</span>,
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
