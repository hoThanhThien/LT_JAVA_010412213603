"use client";

import { useRef, useState, useEffect } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { ProTable } from "@ant-design/pro-components";
import { OrderListType } from "@/schemaValidations/order.schema";
import orderApiRequests from "@/apiRequests/order";
import { Button, Divider, Tag } from "antd";
import { Modal } from "antd/lib";

export default function CustomerAllOrder(props: {
  idCustomer: string;
  openOrder: boolean;
  setOpenOrder: (v: boolean) => void;
  setIdCustomer: (v: string) => void;
}) {
  const { idCustomer, openOrder, setOpenOrder, setIdCustomer } = props;

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

  const refreshTable = () => {
    actionRef.current?.reload();
  };

  // Hàm fetch data riêng để có thể gọi lại khi cần
  const fetchData = async (page: number, size: number, id: string) => {
    setLoading(true);
    try {
      // API thường yêu cầu page bắt đầu từ 0
      console.log(id);

      const apiPage = page - 1;
      const query = await orderApiRequests.adminOrderCustomer(
        apiPage,
        size,
        id
      );
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
          <Button key="add" type="primary">
            Thêm mới
          </Button>,
        ]}
      >
        <Divider />
        <ProTable<OrderListType>
          columns={columns}
          actionRef={actionRef}
          cardBordered
          search={false}
          request={async () => {
            return await fetchData(currentPage, pageSize, idCustomer);
          }}
          rowKey="username"
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
          loading={loading}
        />
      </Modal>
    </>
  );
}
