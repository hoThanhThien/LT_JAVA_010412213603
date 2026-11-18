"use client";

import { useRef, useState } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { ProTable } from "@ant-design/pro-components";
import { OrderListType } from "@/schemaValidations/order.schema";
import orderApiRequests from "@/apiRequests/order";
import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { useAuthStore } from "@/lib/zustand";
import { useRouter } from "next/navigation";
import { useHydrated } from "@/hooks/use-hydrated";
import { formatCurrency } from "@/lib/utils";
import { Plus, ReceiptText } from "lucide-react";

export default function CustomerListOrder() {
  const { openPay, setOpenPay, setOpenOrder } = useAuthStore();
  const router = useRouter();

  const mounted = useHydrated();
  const actionRef = useRef<ActionType>(null);
  const [loading, setLoading] = useState(false);

  const fetchData = async (page: number, size: number) => {
    setLoading(true);
    try {
      const apiPage = page - 1;
      const query = await orderApiRequests.customerListOrder(apiPage, size);
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
      render: (_, entity) => (
        <span className="font-bold text-gray-900">#{entity.id}</span>
      ),
    },
    {
      title: "Cửa hàng",
      dataIndex: "laundryShopName",
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
      title: "Khối lượng",
      dataIndex: "orderVolume",
      hideInSearch: true,
      render: (_, entity) => <span>{entity.orderVolume} kg</span>,
    },
    {
      title: "Trạng thái đơn",
      dataIndex: "orderStatus",
      align: "center",
      render: (_, entity) =>
        entity.orderStatus === "DONE" ? (
          <span className="inline-flex rounded-full bg-green-100 px-3 py-0.5 text-xs font-semibold text-green-800">
            Hoàn thành
          </span>
        ) : (
          <span className="inline-flex rounded-full bg-amber-100 px-3 py-0.5 text-xs font-semibold text-amber-800">
            Đang xử lý
          </span>
        ),
    },
    {
      title: "Tổng tiền",
      dataIndex: "totalAmount",
      hideInSearch: true,
      align: "center",
      render: (_, entity) => (
        <span className="font-bold text-gray-900">
          {formatCurrency(entity.totalAmount)}
        </span>
      ),
    },
    {
      title: "Thanh toán",
      align: "center",
      render: (_, entity) =>
        entity.paymentStatus === "PAID" ? (
          <span className="inline-flex rounded-full bg-green-100 px-3 py-1 text-xs font-bold text-green-800">
            Đã thanh toán
          </span>
        ) : (
          <div className="flex flex-col items-center gap-1.5">
            <span className="inline-flex rounded-full bg-gray-100 px-2.5 py-0.5 text-[11px] font-medium text-gray-600">
              Chờ thanh toán
            </span>
            <button
              type="button"
              onClick={() => {
                setOpenPay(false);
                router.push(`/thanh-toan?orderId=${entity.id}`);
              }}
              className="rounded-full bg-main hover:bg-hover px-4 py-1.5 text-xs font-bold text-black cursor-pointer transition-colors"
            >
              Thanh toán ngay
            </button>
          </div>
        ),
    },
  ];

  return (
    <Dialog
      open={openPay}
      onOpenChange={(open: boolean) => {
        setOpenPay(open);
      }}
    >
      <DialogContent
        className="sm:max-w-[650px] md:max-w-[820px] lg:max-w-[1060px] w-[92vw] max-h-[90vh] overflow-y-auto p-0 rounded-[28px] border-none"
        aria-describedby={undefined}
        onInteractOutside={(e) => {
          e.preventDefault();
        }}
      >
        <div className="bg-black px-6 py-5 text-white flex flex-wrap items-center justify-between gap-4 pr-12">
          <div className="flex items-center gap-3">
            <span className="flex h-10 w-10 items-center justify-center rounded-full bg-main text-black">
              <ReceiptText className="h-5 w-5" />
            </span>
            <div>
              <DialogTitle className="text-xl font-bold text-white">
                Đơn hàng & Thanh toán
              </DialogTitle>
              <p className="text-xs text-gray-300">
                Theo dõi tiến trình xử lý đơn và quét mã VietQR để thanh toán
              </p>
            </div>
          </div>
          <button
            type="button"
            onClick={() => {
              setOpenPay(false);
              setOpenOrder(true);
            }}
            className="inline-flex items-center gap-1.5 rounded-full bg-main hover:bg-hover px-4 py-2 text-sm font-bold text-black cursor-pointer transition-colors"
          >
            <Plus className="h-4 w-4" />
            Đặt dịch vụ mới
          </button>
        </div>

        <div className="p-6">
          {openPay && (
            <ProTable<OrderListType>
              columns={columns}
              actionRef={actionRef}
              cardBordered
              search={false}
              request={async (params) => {
                return await fetchData(
                  params.current ?? 1,
                  params.pageSize ?? 10
                );
              }}
              rowKey="id"
              pagination={{
                defaultPageSize: 10,
                showSizeChanger: true,
              }}
              loading={loading}
            />
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}
