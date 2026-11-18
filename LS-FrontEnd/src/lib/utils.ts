import { clsx, type ClassValue } from "clsx";
import { UseFormSetError } from "react-hook-form";
import { twMerge } from "tailwind-merge";
import { EntityError } from "./http";
import { toast } from "react-toastify";
import authApiRequests from "@/apiRequests/auth";
import type { SessionResType } from "@/schemaValidations/auth.schema";
import dayjs from "dayjs";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Xóa đi ký tự `/` đầu tiên của path
 */
export const normalizePath = (path: string) => {
  return path.startsWith("/") ? path.slice(1) : path;
};

export const handleErrorApi = ({
  error,
  setError,
  duration,
}: {
  error: any;
  setError?: UseFormSetError<any>;
  duration?: number;
}) => {
  if (error instanceof EntityError && setError) {
    error.payload.errors.forEach((item) => {
      setError(item.field, {
        type: "server",
        message: item.message,
      });
    });
  } else {
    toast.error(error?.payload?.message ?? "Lỗi không xác định", {
      autoClose: duration ?? 5000,
    });
  }
};

// Token được lưu trong cookie HttpOnly nên JS không đọc được.
// Ở client chỉ giữ cache "thông tin phiên" (hạn token, role) lấy từ /api/auth/session.
let sessionCache: SessionResType | null = null;
let sessionRequest: Promise<SessionResType> | null = null;

export const resetSessionCache = () => {
  sessionCache = null;
};

export const getSession = async (): Promise<SessionResType> => {
  if (sessionCache) return sessionCache;
  if (!sessionRequest) {
    sessionRequest = authApiRequests
      .session()
      .then((res) => res.payload)
      .finally(() => {
        sessionRequest = null;
      });
  }
  sessionCache = await sessionRequest;
  return sessionCache;
};

export const checkAndRefreshToken = async (param?: {
  onError?: () => void;
  onSuccess?: () => void;
}) => {
  const session = await getSession();
  //Chưa đăng nhập cx k cho chạy
  if (!session.authenticated) return;

  const now = Math.round(new Date().getTime() / 1000); // current time in seconds
  const accessExp = session.accessExp ?? 0;
  const accessIat = session.accessIat ?? 0;

  //trường hợp refresh token hết hạn thì không xử lý (cookie cũng đã tự hết hạn)
  if ((session.refreshExp ?? 0) < now) {
    resetSessionCache();
    return param?.onError && param.onError();
  }

  // accessExp = 0 nghĩa là cookie access token đã hết hạn => cần refresh ngay
  if (accessExp - now < (accessExp - accessIat) / 3) {
    try {
      await authApiRequests.refreshToken();
      resetSessionCache();
      await getSession();
      param?.onSuccess && param.onSuccess();
    } catch (error) {
      resetSessionCache();
      param?.onError && param.onError();
    }
  }
};
export const formatDate = (day: any) => {
  return dayjs(day).format("DD-MM-YYYY");
};
