"use client";

import paymentApiRequests from "@/apiRequests/payment";
import { formatCurrency } from "@/lib/utils";
import { PaymentType } from "@/schemaValidations/payment.schema";
import {
  ArrowLeft,
  CheckCircle2,
  Clock,
  Copy,
  RefreshCw,
  ShieldCheck,
  XCircle,
} from "lucide-react";
import Image from "next/image";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { Suspense, useCallback, useEffect, useState } from "react";
import { toast } from "react-toastify";

function ThanhToan() {
  const [paymentData, setPaymentData] = useState<PaymentType | null>(null);
  const [loading, setLoading] = useState(true);
  const [checking, setChecking] = useState(false);
  const params = useSearchParams();

  const orderIdParam = params.get("orderId");
  const parsedId = orderIdParam ? Number(orderIdParam) : NaN;
  const isValidOrderId = Number.isInteger(parsedId) && parsedId > 0;

  useEffect(() => {
    let cancelled = false;
    let isPaid = false;
    let hasLoaded = false;
    let inFlight = false;

    const fetchData = async (): Promise<boolean> => {
      if (cancelled || inFlight) return false;
      if (!isValidOrderId) {
        isPaid = true;
        setLoading(false);
        return true;
      }

      inFlight = true;
      try {
        if (!hasLoaded) {
          setLoading(true);
        }
        const result = await paymentApiRequests.get(parsedId);
        if (cancelled) return false;
        setPaymentData(result.payload);
        hasLoaded = true;
        if (result.payload.paymentStatus === "PAID") {
          isPaid = true;
          return true;
        }
      } catch (error) {
        console.error("Error fetching payment data:", error);
      } finally {
        inFlight = false;
        if (!cancelled) {
          setLoading(false);
        }
      }
      return false;
    };

    void fetchData();

    const intervalId = setInterval(() => {
      if (cancelled || isPaid) {
        clearInterval(intervalId);
        return;
      }
      void fetchData().then((paid) => {
        if (paid) {
          clearInterval(intervalId);
        }
      });
    }, 5000);

    return () => {
      cancelled = true;
      clearInterval(intervalId);
    };
  }, [isValidOrderId, parsedId]);

  const handleManualCheck = useCallback(async () => {
    if (!isValidOrderId || checking) return;
    setChecking(true);
    try {
      const result = await paymentApiRequests.get(parsedId);
      setPaymentData(result.payload);
      if (result.payload.paymentStatus === "PAID") {
        toast.success("Đơn hàng đã được xác nhận thanh toán thành công!");
      } else {
        toast.info(
          "Hệ thống chưa ghi nhận giao dịch mới, vui lòng đợi vài giây rồi kiểm tra lại."
        );
      }
    } catch {
      toast.error("Không thể kiểm tra trạng thái thanh toán lúc này.");
    } finally {
      setChecking(false);
    }
  }, [checking, isValidOrderId, parsedId]);

  const handleCopy = async (label: string, value: string) => {
    try {
      await navigator.clipboard.writeText(value);
      toast.success(`Đã sao chép ${label}`);
    } catch {
      toast.error("Trình duyệt không hỗ trợ sao chép tự động");
    }
  };

  const isPaid = paymentData?.paymentStatus === "PAID";

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <header className="bg-black text-white border-b border-neutral-800">
        <div className="max-w-5xl mx-auto px-4 h-20 flex items-center justify-between gap-4">
          <div className="flex items-center gap-4">
            <Link href="/" className="flex items-center gap-3">
              <Image
                src="/heramo-logo.png"
                alt="HERAMO Logo"
                width={140}
                height={42}
                className="w-auto h-10"
              />
            </Link>
            <div className="hidden sm:block h-6 w-px bg-neutral-700" />
            <span className="hidden sm:inline text-base font-semibold text-white">
              Thanh toán đơn hàng
            </span>
          </div>
          <Link
            href="/"
            className="inline-flex items-center gap-2 rounded-[28px] border border-main px-5 py-2 text-sm font-bold text-main hover:bg-main hover:text-black transition-colors"
          >
            <ArrowLeft className="h-4 w-4" />
            Về trang chủ
          </Link>
        </div>
      </header>

      <main className="flex-1 max-w-5xl w-full mx-auto px-4 py-8 sm:py-10">
        <div className="mb-8 flex flex-wrap items-center justify-center gap-3 sm:gap-6 text-sm font-semibold">
          <div className="flex items-center gap-2 text-black">
            <span className="flex h-7 w-7 items-center justify-center rounded-full bg-main text-black font-bold">
              1
            </span>
            <span>Đặt dịch vụ</span>
          </div>
          <div className="h-px w-8 bg-gray-300" />
          <div className="flex items-center gap-2 text-black">
            <span className="flex h-7 w-7 items-center justify-center rounded-full bg-main text-black font-bold">
              2
            </span>
            <span>Thanh toán VietQR</span>
          </div>
          <div className="h-px w-8 bg-gray-300" />
          <div
            className={`flex items-center gap-2 ${
              isPaid ? "text-black" : "text-gray-400"
            }`}
          >
            <span
              className={`flex h-7 w-7 items-center justify-center rounded-full font-bold ${
                isPaid ? "bg-main text-black" : "bg-gray-200 text-gray-500"
              }`}
            >
              3
            </span>
            <span>Xử lý & Giao nhận</span>
          </div>
        </div>

        {loading && !paymentData ? (
          <div className="rounded-[32px] bg-white shadow-xl border border-gray-100 p-12 flex flex-col items-center justify-center text-center">
            <div className="h-12 w-12 rounded-full border-4 border-main border-t-transparent animate-spin" />
            <p className="mt-4 text-base font-medium text-gray-700">
              Đang tải thông tin thanh toán đơn hàng...
            </p>
          </div>
        ) : !paymentData ? (
          <div className="rounded-[32px] bg-white shadow-xl border border-gray-100 p-10 text-center max-w-xl mx-auto">
            <div className="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-red-50 text-red-500">
              <XCircle className="h-9 w-9" />
            </div>
            <h1 className="text-2xl font-bold text-gray-900">
              Không thể tải thông tin thanh toán
            </h1>
            <p className="mt-2 text-sm text-gray-500">
              Đơn hàng không tồn tại hoặc bạn không có quyền truy cập thông tin
              thanh toán này.
            </p>
            <Link
              href="/"
              className="mt-6 inline-flex items-center justify-center rounded-[28px] bg-main hover:bg-hover px-8 py-3 text-base font-bold text-black transition-colors"
            >
              Về trang chủ
            </Link>
          </div>
        ) : isPaid ? (
          <div className="rounded-[32px] bg-white shadow-xl border border-gray-100 p-10 text-center max-w-xl mx-auto">
            <div className="mx-auto mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-main text-black">
              <CheckCircle2 className="h-9 w-9" />
            </div>
            <h1 className="text-2xl font-bold text-gray-900">
              Thanh toán đơn hàng #{paymentData.orderId} thành công!
            </h1>
            <p className="mt-2 text-base text-gray-600">
              HERAMO đã nhận được khoản thanh toán{" "}
              <span className="font-bold text-black">
                {formatCurrency(paymentData.totalAmount)}
              </span>
              . Shipper và cửa hàng sẽ sớm liên hệ xử lý đơn của bạn.
            </p>
            <div className="mt-8 flex flex-wrap justify-center gap-4">
              <Link
                href="/"
                className="inline-flex items-center justify-center rounded-[28px] bg-main hover:bg-hover px-8 py-3 text-base font-bold text-black transition-colors"
              >
                Về trang chủ
              </Link>
            </div>
          </div>
        ) : (
          <div className="rounded-[32px] bg-white shadow-xl border border-gray-100 p-6 sm:p-10">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8 items-center">
              <div className="flex flex-col items-center">
                <div className="inline-flex items-center gap-2 rounded-full bg-amber-50 border border-amber-200 px-4 py-1.5 text-xs font-semibold text-amber-800 mb-4">
                  <Clock className="h-3.5 w-3.5 animate-pulse" />
                  Tự động kiểm tra mỗi 5 giây
                </div>

                {paymentData.qrCode && (
                  <div className="rounded-3xl border-2 border-main p-4 bg-white shadow-sm">
                    <Image
                      src={paymentData.qrCode}
                      alt="QR Code thanh toán VietQR"
                      width={280}
                      height={280}
                      unoptimized
                      className="mx-auto rounded-xl"
                    />
                  </div>
                )}

                <p className="mt-4 text-center text-xs text-gray-500 max-w-xs">
                  Mở ứng dụng ngân hàng hoặc ví điện tử bất kỳ để quét mã
                  VietQR. Số tiền và nội dung chuyển khoản sẽ tự động điền.
                </p>

                <button
                  type="button"
                  disabled={checking}
                  onClick={handleManualCheck}
                  className="mt-4 inline-flex items-center gap-2 rounded-[28px] border border-gray-300 px-5 py-2 text-sm font-semibold text-gray-800 hover:border-black cursor-pointer transition-colors disabled:opacity-50"
                >
                  <RefreshCw
                    className={`h-4 w-4 ${checking ? "animate-spin" : ""}`}
                  />
                  {checking ? "Đang kiểm tra..." : "Kiểm tra thanh toán ngay"}
                </button>
              </div>

              <div className="flex flex-col justify-between space-y-5">
                <div>
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium text-gray-500">
                      Mã đơn hàng
                    </span>
                    <span className="rounded-full bg-black px-3.5 py-1 text-sm font-bold text-main">
                      #{paymentData.orderId}
                    </span>
                  </div>
                  <div className="mt-4 rounded-2xl bg-black px-5 py-4 text-white flex items-center justify-between">
                    <span className="text-sm text-gray-300">
                      Số tiền cần thanh toán
                    </span>
                    <span className="text-2xl font-bold text-main">
                      {formatCurrency(paymentData.totalAmount)}
                    </span>
                  </div>
                </div>

                <div className="divide-y divide-gray-100 rounded-2xl border border-gray-200 bg-gray-50/60 px-4">
                  <div className="flex items-center justify-between py-3 text-sm">
                    <span className="text-gray-500">Ngân hàng</span>
                    <span className="font-semibold text-gray-900">
                      {paymentData.bankName}
                    </span>
                  </div>
                  <div className="flex items-center justify-between py-3 text-sm">
                    <span className="text-gray-500">Chủ tài khoản</span>
                    <span className="font-semibold text-gray-900">
                      {paymentData.accountHolder}
                    </span>
                  </div>
                  <div className="flex items-center justify-between py-3 text-sm gap-2">
                    <span className="text-gray-500">Số tài khoản</span>
                    <div className="flex items-center gap-2">
                      <span className="font-bold text-gray-900">
                        {paymentData.accountNumber}
                      </span>
                      <button
                        type="button"
                        onClick={() =>
                          handleCopy("số tài khoản", paymentData.accountNumber)
                        }
                        className="inline-flex items-center gap-1 rounded-full bg-white border border-gray-200 px-2.5 py-1 text-xs font-semibold text-gray-700 hover:border-main cursor-pointer"
                      >
                        <Copy className="h-3 w-3" />
                        Sao chép
                      </button>
                    </div>
                  </div>
                  <div className="flex items-center justify-between py-3 text-sm gap-2">
                    <span className="text-gray-500">Nội dung CK</span>
                    <div className="flex items-center gap-2">
                      <span className="font-bold text-black bg-amber-100 px-2 py-0.5 rounded">
                        {paymentData.transferContent}
                      </span>
                      <button
                        type="button"
                        onClick={() =>
                          handleCopy(
                            "nội dung chuyển khoản",
                            paymentData.transferContent
                          )
                        }
                        className="inline-flex items-center gap-1 rounded-full bg-white border border-gray-200 px-2.5 py-1 text-xs font-semibold text-gray-700 hover:border-main cursor-pointer"
                      >
                        <Copy className="h-3 w-3" />
                        Sao chép
                      </button>
                    </div>
                  </div>
                </div>

                <div className="flex items-center gap-2 text-xs text-gray-500">
                  <ShieldCheck className="h-4 w-4 text-green-600 shrink-0" />
                  <span>
                    Giữ nguyên nội dung chuyển khoản{" "}
                    <strong className="text-gray-800">
                      {paymentData.transferContent}
                    </strong>{" "}
                    để hệ thống tự động đối soát chính xác.
                  </span>
                </div>

                <div className="flex flex-col sm:flex-row gap-3 pt-1">
                  <button
                    type="button"
                    disabled={checking}
                    onClick={handleManualCheck}
                    className="flex-1 rounded-[28px] bg-main hover:bg-hover px-6 py-3 text-sm font-bold text-black cursor-pointer transition-colors disabled:opacity-50"
                  >
                    Tôi đã chuyển khoản
                  </button>
                  <Link
                    href="/"
                    className="flex-1 inline-flex items-center justify-center rounded-[28px] bg-black hover:bg-neutral-800 px-6 py-3 text-sm font-bold text-white transition-colors"
                  >
                    Thanh toán sau / Về trang chủ
                  </Link>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}

export default function page() {
  return (
    <Suspense
      fallback={
        <div className="h-screen flex items-center justify-center bg-gray-50">
          <div className="h-10 w-10 rounded-full border-4 border-main border-t-transparent animate-spin" />
        </div>
      }
    >
      <ThanhToan />
    </Suspense>
  );
}
