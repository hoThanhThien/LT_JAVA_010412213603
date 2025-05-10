"use client";

import paymentApiRequests from "@/apiRequests/payment";
import { PaymentType } from "@/schemaValidations/payment.schema";
import { Button, Result, Spin } from "antd";
import Image from "next/image";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { Suspense, useEffect, useState } from "react";

function ThanhToan() {
  const [paymentData, setPaymentData] = useState<PaymentType | null>(null);
  const [loading, setLoading] = useState(true);
  const params = useSearchParams();

  useEffect(() => {
    const fetchData = async () => {
      const orderId = params.get("orderId");
      if (orderId) {
        try {
          setLoading(true);
          const result = await paymentApiRequests.get(parseInt(orderId));
          setPaymentData(result.payload);
        } catch (error) {
          console.error("Error fetching payment data:", error);
        } finally {
          setLoading(false);
        }
      }
    };

    fetchData();

    // Thiết lập interval để kiểm tra trạng thái mỗi 5 giây
    const intervalId = setInterval(() => {
      console.log("check");

      fetchData();
    }, 5000);

    // Xóa interval khi component unmount
    return () => clearInterval(intervalId);
  }, [params]);

  if (loading && !paymentData) {
    return (
      <div className="h-screen flex justify-center items-center flex-col">
        <Spin size="large" />
        <p className="mt-4">Đang tải thông tin thanh toán...</p>
      </div>
    );
  }

  if (!paymentData) {
    return (
      <div className="h-screen flex justify-center items-center flex-col">
        <Result
          status="error"
          title="Không thể tải thông tin thanh toán"
          subTitle="Vui lòng thử lại sau hoặc liên hệ hỗ trợ"
          extra={[
            <Button type="primary" key="home">
              <Link href="/">Về trang chủ</Link>
            </Button>,
          ]}
        />
      </div>
    );
  }

  const {
    orderId,
    totalAmount,
    accountHolder,
    bankName,
    accountNumber,
    transferContent,
    paymentStatus,
    qrCode,
  } = paymentData;

  return (
    <>
      {paymentStatus !== "PAID" ? (
        <div className="max-w-md mx-auto bg-white rounded-xl shadow-lg overflow-hidden md:max-w-2xl p-4">
          <div className="text-center mb-4">
            <h2 className="text-xl font-bold text-gray-800">
              Chi tiết thanh toán
            </h2>
          </div>

          <div className="flex flex-col items-center mb-4">
            {qrCode && (
              <div className="mb-4">
                <Image
                  src={qrCode}
                  alt="QR Code thanh toán"
                  width={300}
                  height={300}
                  className="mx-auto"
                />
              </div>
            )}
          </div>

          <div className="border-t border-gray-200 pt-4">
            <div className="grid grid-cols-2 gap-2 text-center">
              <div className="text-gray-600">Mã đơn hàng:</div>
              <div className="font-medium">{orderId}</div>

              <div className="text-gray-600">Số tiền:</div>
              <div className="font-medium">
                {new Intl.NumberFormat("vi-VN", {
                  style: "currency",
                  currency: "VND",
                }).format(totalAmount)}
              </div>

              <div className="text-gray-600">Chủ tài khoản:</div>
              <div className="font-medium">{accountHolder}</div>

              <div className="text-gray-600">Ngân hàng:</div>
              <div className="font-medium">{bankName}</div>

              <div className="text-gray-600">Số tài khoản:</div>
              <div className="font-medium">{accountNumber}</div>

              <div className="text-gray-600">Nội dung chuyển khoản:</div>
              <div className="font-medium">{transferContent}</div>
            </div>
          </div>

          <div className="mt-4 text-center">
            <p className="text-sm text-gray-600">
              Vui lòng quét mã QR hoặc chuyển khoản theo thông tin trên để hoàn
              tất thanh toán
            </p>
            <p className="text-xs text-gray-500 mt-2">
              (Trang sẽ tự động cập nhật khi thanh toán hoàn tất)
            </p>
          </div>
        </div>
      ) : (
        <Result
          status="success"
          className="h-screen flex justify-center items-center flex-col"
          title="Đơn hàng của bạn đã được thanh toán thành công"
          subTitle="Cảm ơn bạn đã tin tưởng sử dụng dịch vụ của chúng tôi"
          extra={[
            <Button type="primary" key="console">
              <Link href="/">Về trang chủ</Link>
            </Button>,
          ]}
        />
      )}
    </>
  );
}

export default function page() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <ThanhToan />
    </Suspense>
  );
}
