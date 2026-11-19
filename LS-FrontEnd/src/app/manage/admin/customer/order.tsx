"use client";

import { useRef, useState } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { ProTable } from "@ant-design/pro-components";
import { OrderListType } from "@/schemaValidations/order.schema";
import orderApiRequests from "@/apiRequests/order";
import { Button, Divider, Tag } from "antd";
import { Modal } from "antd/lib";
import { useHydrated } from "@/hooks/use-hydrated";
import { formatCurrency } from "@/lib/utils";

export default function CustomerAllOrder(props: {
  idCustomer: string;
  openOrder: boolean;
  setOpenOrder: (v: boolean) => void;
  setIdCustomer: (v: string) => void;
}) {
  const { idCustomer, openOrder, setOpenOrder, setIdCustomer } = props;

  const mounted = useHydrated();
  const actionRef = useRef<ActionType>(null);
  const [loading, setLoading] = useState(false);

  // Hàm fetch data riêng để có thể gọi lại khi cần
  const fetchData = async (page: number, size: number, id: string) => {
    if (!id) {
      return { data: [], success: true, total: 0 };
    }
    setLoading(true);
    try {
      // API thường yêu cầu page bắt đầu từ 0
      const apiPage = page - 1;
      const query = await orderApiRequests.adminOrderCustomer(
        apiPage,
        size,
        id
      );
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
      title: "Mã đơn",
      dataIndex: "id",
      copyable: true,
      hideInSearch: true,
    },
    {
      title: "Dịch vụ",
      hideInSearch: true,
      render: (_, entity) => (
        <div>
          {entity.serviceCategoryName} - {entity.serviceName}
        </div>
      ),
    },
    {
      title: "Trạng thái",
      dataIndex: "orderStatus",
      align: "center",
      render: (_, entity) => (
        <Tag color={entity.orderStatus === "DONE" ? "green" : "yellow"}>
          {entity.orderStatus}
        </Tag>
      ),
    },
    {
      title: "Khối lượng",
      dataIndex: "orderVolume",
      hideInSearch: true,
    },
    {
      title: "Tổng tiền",
      dataIndex: "totalAmount",
      hideInSearch: true,
      render: (_, entity) => <span>{formatCurrency(entity.totalAmount)}</span>,
    },
  ];

  return (
    <>
      <Modal
        title="Lịch sử đơn hàng"
        open={openOrder}
        width={1000}
        onCancel={() => {
          setOpenOrder(false);
          setIdCustomer("");
        }}
        destroyOnClose={true}
        footer={[
          <Button
            key="close"
            type="primary"
            onClick={() => {
              setOpenOrder(false);
              setIdCustomer("");
            }}
          >
            Đóng
          </Button>,
        ]}
      >
        <Divider />
        <ProTable<OrderListType>
          columns={columns}
          actionRef={actionRef}
          cardBordered
          search={false}
          params={{ idCustomer }}
          request={async (params) => {
            return await fetchData(
              params.current ?? 1,
              params.pageSize ?? 10,
              params.idCustomer
            );
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
          loading={loading}
        />
      </Modal>
    </>
  );
}
