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
let refreshCheckPromise: Promise<void> | null = null;
const REFRESH_COOLDOWN_KEY = "heramo_rt_last_attempt";
const REFRESH_COOLDOWN_MS = 5000;

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
  if (refreshCheckPromise) return refreshCheckPromise;

  refreshCheckPromise = (async () => {
    let session: SessionResType;
    try {
      session = await getSession();
    } catch {
      return;
    }
    // Chưa đăng nhập thì không cần làm mới token
    if (!session.authenticated) return;

    const now = Math.round(Date.now() / 1000);
    const accessExp = session.accessExp ?? 0;
    const accessIat = session.accessIat ?? 0;

    // Trường hợp refresh token hết hạn thì xóa cache phiên cục bộ
    if ((session.refreshExp ?? 0) > 0 && (session.refreshExp ?? 0) < now) {
      resetSessionCache();
      param?.onError?.();
      return;
    }

    const needsRefresh =
      accessExp <= 0 ||
      accessExp <= accessIat ||
      accessExp - now < (accessExp - accessIat) / 3;

    if (!needsRefresh) return;

    // Tránh nhiều tab cùng gọi rotate refresh token trong vài giây
    try {
      const lastAttempt = Number(
        typeof window !== "undefined"
          ? window.localStorage.getItem(REFRESH_COOLDOWN_KEY) ?? "0"
          : "0"
      );
      if (Date.now() - lastAttempt < REFRESH_COOLDOWN_MS) {
        resetSessionCache();
        await getSession();
        return;
      }
      if (typeof window !== "undefined") {
        window.localStorage.setItem(REFRESH_COOLDOWN_KEY, String(Date.now()));
      }
    } catch {
      // Bỏ qua nếu trình duyệt chặn localStorage
    }

    try {
      await authApiRequests.refreshToken();
      resetSessionCache();
      await getSession();
      param?.onSuccess?.();
    } catch {
      resetSessionCache();
      param?.onError?.();
    }
  })().finally(() => {
    refreshCheckPromise = null;
  });

  return refreshCheckPromise;
};
export const formatDate = (day: any) => {
  return dayjs(day).format("DD-MM-YYYY");
};

export const formatCurrency = (amount?: number | null) => {
  if (amount == null || !Number.isFinite(amount)) return "0 đ";
  return `${new Intl.NumberFormat("vi-VN").format(amount)} đ`;
};

const readFileAsDataUrl = (file: File): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(String(reader.result ?? ""));
    reader.onerror = (error) => reject(error);
  });

export const compressImageToBase64 = async (
  file: File,
  maxDimension = 800,
  quality = 0.78
): Promise<string> => {
  if (!file.type.startsWith("image/")) {
    throw new Error("Vui lòng chọn tệp hình ảnh hợp lệ");
  }
  if (file.size > 10 * 1024 * 1024) {
    throw new Error("Kích thước ảnh không được vượt quá 10MB");
  }
  if (file.type === "image/svg+xml" || file.size <= 120 * 1024) {
    return readFileAsDataUrl(file);
  }

  const objectUrl = URL.createObjectURL(file);
  try {
    const img = await new Promise<HTMLImageElement>((resolve, reject) => {
      const image = new window.Image();
      image.onload = () => resolve(image);
      image.onerror = (err) => reject(err);
      image.src = objectUrl;
    });

    const scale = Math.min(1, maxDimension / Math.max(img.width, img.height));
    const width = Math.max(1, Math.round(img.width * scale));
    const height = Math.max(1, Math.round(img.height * scale));

    const canvas = document.createElement("canvas");
    canvas.width = width;
    canvas.height = height;
    const ctx = canvas.getContext("2d");
    if (!ctx) {
      return readFileAsDataUrl(file);
    }
    ctx.drawImage(img, 0, 0, width, height);
    const outputType = file.type === "image/png" ? "image/webp" : "image/jpeg";
    return canvas.toDataURL(outputType, quality);
  } catch {
    return readFileAsDataUrl(file);
  } finally {
    URL.revokeObjectURL(objectUrl);
  }
};
