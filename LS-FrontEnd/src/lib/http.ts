import envConfig from "@/config";
import { normalizePath, resetSessionCache } from "@/lib/utils";
import { useAuthStore } from "@/lib/zustand";
import { redirect } from "next/navigation";

type CustomOptions = Omit<RequestInit, "method"> & {
  baseUrl?: string | undefined;
};

const ENTITY_ERROR_STATUS = 422;
const AUTHENTICATION_ERROR_STATUS = 401;

type EntityErrorPayload = {
  message: string;
  errors: {
    field: string;
    message: string;
  }[];
};

export class HttpError extends Error {
  status: number;
  payload: {
    message: string;
    [key: string]: any;
  };
  constructor({
    status,
    payload,
    message,
  }: {
    status: number;
    payload: any;
    message?: string;
  }) {
    super(
      message ??
        (typeof payload?.message === "string" && payload.message
          ? payload.message
          : "Lỗi HTTP")
    );
    this.status = status;
    this.payload = payload;
  }
}

export class EntityError extends HttpError {
  status: typeof ENTITY_ERROR_STATUS;
  payload: EntityErrorPayload;
  constructor({
    status,
    payload,
  }: {
    status: typeof ENTITY_ERROR_STATUS;
    payload: EntityErrorPayload;
  }) {
    super({ status, payload, message: "Lỗi thực thể" });
    this.status = status;
    this.payload = payload;
  }
}

let clientLogoutRequest: null | Promise<any> = null;
const isClient = typeof window !== "undefined";
const request = async <Response>(
  method: "GET" | "POST" | "PUT" | "DELETE",
  url: string,
  options?: CustomOptions | undefined
) => {
  let body: FormData | string | undefined = undefined;
  if (options?.body instanceof FormData) {
    body = options.body;
  } else if (options?.body) {
    body = JSON.stringify(options.body);
  }
  const baseHeaders: {
    [key: string]: string;
  } =
    body instanceof FormData
      ? {}
      : {
          "Content-Type": "application/json",
        };
  // Token nằm trong cookie HttpOnly nên JS ở trình duyệt không đọc/gắn được.
  // Ở client, mọi request tới backend đi qua proxy /api/backend (server Next gắn Authorization).
  if (isClient && options?.baseUrl === undefined) {
    url = `api/backend/${normalizePath(url)}`;
    options = { ...options, baseUrl: "" };
  }
  // Nếu không truyền baseUrl (hoặc baseUrl = undefined) thì lấy từ envConfig.NEXT_PUBLIC_API_ENDPOINT
  // Nếu truyền baseUrl thì lấy giá trị truyền vào, truyền vào '' thì đồng nghĩa với việc chúng ta gọi API đến Next.js Server

  const baseUrl =
    options?.baseUrl === undefined
      ? envConfig.NEXT_PUBLIC_API_ENDPOINT
      : options.baseUrl;

  const fullUrl = `${baseUrl}/${normalizePath(url)}`;
  const res = await fetch(fullUrl, {
    ...options,
    headers: {
      ...baseHeaders,
      ...options?.headers,
    } as any,
    body,
    method,
  });
  const rawText = await res.text();
  let parsedPayload: any = null;
  if (rawText) {
    try {
      parsedPayload = JSON.parse(rawText);
    } catch {
      parsedPayload = { message: rawText };
    }
  }
  const payload = (parsedPayload ?? {
    message: res.statusText || "Lỗi HTTP",
  }) as Response;
  const data = {
    status: res.status,
    payload,
  };
  // Interceptor là nời chúng ta xử lý request và response trước khi trả về cho phía component
  const normalizeUrl = normalizePath(url);
  const isAuthEndpoint = [
    "api/auth/login",
    "api/auth/google",
    "api/auth/register",
    "api/auth/refresh-token",
    "api/auth/logout",
    "auth/login",
    "auth/google",
    "auth/register",
    "auth/refresh-token",
    "auth/logout",
  ].includes(normalizeUrl);

  if (!res.ok) {
    if (res.status === ENTITY_ERROR_STATUS) {
      throw new EntityError(
        data as {
          status: 422;
          payload: EntityErrorPayload;
        }
      );
    } else if (res.status === AUTHENTICATION_ERROR_STATUS) {
      if (isAuthEndpoint) {
        throw new HttpError(data);
      }
      if (isClient) {
        resetSessionCache();
        useAuthStore.getState().setIsAuth(false);
        if (!clientLogoutRequest) {
          clientLogoutRequest = fetch("/api/auth/logout", {
            method: "POST",
            body: null,
            headers: {
              ...baseHeaders,
            } as any,
          });
          try {
            await clientLogoutRequest;
          } catch {
          } finally {
            resetSessionCache();
            clientLogoutRequest = null;
            const currentPath = window.location.pathname;
            // Chỉ chuyển hướng khi đang ở trang bắt buộc đăng nhập; tránh reload loop trên trang công khai
            if (
              currentPath.startsWith("/manage") ||
              currentPath.startsWith("/thanh-toan")
            ) {
              window.location.assign("/?login=true");
            }
          }
        }
        throw new HttpError(data);
      } else {
        redirect("/logout");
      }
    } else {
      throw new HttpError(data);
    }
  }
  // Đảm bảo logic dưới đây chỉ chạy ở phía client (browser)
  if (isClient) {
    // Cookie đã được server Next set/xóa; chỉ cần làm mới cache phiên ở client
    if (
      normalizeUrl === "api/auth/login" ||
      normalizeUrl === "api/auth/google" ||
      normalizeUrl === "api/auth/register" ||
      normalizeUrl === "api/auth/logout"
    ) {
      resetSessionCache();
    }
  }
  return data;
};

const http = {
  get<Response>(
    url: string,
    options?: Omit<CustomOptions, "body"> | undefined
  ) {
    return request<Response>("GET", url, options);
  },
  post<Response>(
    url: string,
    body: any,
    options?: Omit<CustomOptions, "body"> | undefined
  ) {
    return request<Response>("POST", url, { ...options, body });
  },
  put<Response>(
    url: string,
    body: any,
    options?: Omit<CustomOptions, "body"> | undefined
  ) {
    return request<Response>("PUT", url, { ...options, body });
  },
  delete<Response>(
    url: string,
    options?: Omit<CustomOptions, "body"> | undefined
  ) {
    return request<Response>("DELETE", url, { ...options });
  },
};

export default http;
