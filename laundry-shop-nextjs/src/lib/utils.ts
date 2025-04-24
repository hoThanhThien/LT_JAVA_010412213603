import { clsx, type ClassValue } from "clsx";
import { UseFormSetError } from "react-hook-form";
import { twMerge } from "tailwind-merge";
import { EntityError } from "./http";
import { toast } from "sonner";
import authApiRequests from "@/apiRequests/auth";
import jwt from "jsonwebtoken";
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
      duration: duration ?? 5000,
    });
  }
};

const isBrowser = typeof window !== "undefined";

export const getAccessTokenFromLocalStorage = () => {
  return isBrowser ? localStorage.getItem("accessToken") : null;
};

export const getRefreshTokenFromLocalStorage = () => {
  return isBrowser ? localStorage.getItem("refreshToken") : null;
};

export const setAccessTokenToLocalStorage = (token: string) => {
  if (isBrowser) localStorage.setItem("accessToken", token);
};

export const setRefreshTokenToLocalStorage = (token: string) => {
  if (isBrowser) localStorage.setItem("refreshToken", token);
};

export const removeTokenFromLocalStorage = () => {
  isBrowser && localStorage.removeItem("accessToken");
  isBrowser && localStorage.removeItem("refreshToken");
};

export const checkAndRefreshToken = async (param?: {
  onError?: () => void;
  onSuccess?: () => void;
}) => {
  //không nên đưa logic lấy access và refresh token ra khỏi func này
  //Vì để mỗi lần mà checkandrefreshToken dc chạy thì chúng ta sẽ có 1 access token và refresh token mới
  //Tránh bug lấy lần đầu , gọi cho các lần sau
  const accessToken = getAccessTokenFromLocalStorage();
  const refreshToken = getRefreshTokenFromLocalStorage();
  //Chưa đăng nhập cx k cho chạy
  if (!accessToken || !refreshToken) return;

  const decodedAccessToken = jwt.decode(accessToken) as {
    exp: number;
    iat: number;
  };
  const decodedRefreshToken = jwt.decode(refreshToken) as {
    exp: number;
    iat: number;
  };

  const now = Math.round(new Date().getTime() / 1000); // current time in seconds

  //trường hợp refresh token hết hạn thì không xử lý
  if (decodedRefreshToken.exp < now) {
    removeTokenFromLocalStorage();
    return param?.onError && param.onError();
  }

  if (
    decodedAccessToken.exp - now <
    (decodedAccessToken.exp - decodedAccessToken.iat) / 3
  ) {
    try {
      const res = await authApiRequests.refreshToken();
      setAccessTokenToLocalStorage(res.payload.data.accessToken);
      setRefreshTokenToLocalStorage(res.payload.data.refreshToken);
      param?.onSuccess && param.onSuccess();
    } catch (error) {
      param?.onError && param.onError();
    }
  }
};

export const formatDate = (day: any) => {
  return dayjs(day).format("DD-MM-YYYY");
};
