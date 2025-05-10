"use client";

import { useRef, useState, useEffect } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { ProTable } from "@ant-design/pro-components";
import { OrderListType } from "@/schemaValidations/order.schema";
import orderApiRequests from "@/apiRequests/order";
import { Button, Tag } from "antd";
import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { useAuthStore } from "@/lib/zustand";
import { useRouter } from "next/navigation";

export default function CustomerListOrder() {
  const { openPay, setOpenPay } = useAuthStore();
  const router = useRouter();

  const [mounted, setMounted] = useState(false);
  const actionRef = useRef<ActionType>(null);
  const [meta, setMeta] = useState({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0,
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  // Hàm fetch data riêng để có thể gọi lại khi cần
  const fetchData = async () => {
    setLoading(true);
    try {
      // API thường yêu cầu page bắt đầu từ 0
      const query = await orderApiRequests.customerListOrder();
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
      align: "center",
    },
    {
      title: "Thanh toán",
      render: (_, entity) =>
        entity.paymentStatus == "PAID" ? (
          <Tag color="green">Đã thanh toán</Tag>
        ) : (
          <Button
            onClick={() => {
              router.push(`/thanh-toan?orderId=${entity.id}`);
            }}
            color="cyan"
            variant="solid"
          >
            Thanh toán
          </Button>
        ),
    },
  ];

  return (
    <>
      <Dialog
        open={openPay}
        onOpenChange={(open: boolean) => {
          setOpenPay(open);
        }}
      >
        <DialogContent
          className="sm:max-w-[600px] md:max-w-[700px] lg:max-w-[1000px] w-[90vw] max-h-[90vh] overflow-y-auto"
          aria-describedby={undefined}
          onInteractOutside={(e) => {
            e.preventDefault();
          }}
        >
          <div className=" flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
            <DialogTitle className="z-500 sm:mx-auto sm:w-full sm:max-w-sm">
              <div className="mb-5 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
                Thanh toán
              </div>
            </DialogTitle>
            <ProTable<OrderListType>
              columns={columns}
              actionRef={actionRef}
              cardBordered
              search={false}
              request={async () => {
                return await fetchData();
              }}
              rowKey="id"
              pagination={false}
              loading={loading}
            />
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
