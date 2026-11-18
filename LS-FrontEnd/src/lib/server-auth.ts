import { cookies } from "next/headers";
import jwt from "jsonwebtoken";
import authApiRequests from "@/apiRequests/auth";
import envConfig from "@/config";
import { HttpError } from "@/lib/http";

export const ACCESS_COOKIE = "accessToken";
export const REFRESH_COOKIE = "refreshToken";

const cookieOptions = (token: string) => {
  const decoded = jwt.decode(token) as { exp?: number } | null;
  return {
    path: "/",
    httpOnly: true,
    sameSite: "strict" as const,
    secure: envConfig.NEXT_PUBLIC_URL.startsWith("https://"),
    ...(decoded?.exp ? { expires: decoded.exp * 1000 } : {}),
  };
};

/** Lưu cặp token vào cookie HttpOnly + Secure (JS ở trình duyệt không đọc được). */
export async function setAuthCookies(accessToken: string, refreshToken: string) {
  const store = await cookies();
  store.set(ACCESS_COOKIE, accessToken, cookieOptions(accessToken));
  store.set(REFRESH_COOKIE, refreshToken, cookieOptions(refreshToken));
}

export async function clearAuthCookies() {
  const store = await cookies();
  store.delete(ACCESS_COOKIE);
  store.delete(REFRESH_COOKIE);
}

/**
 * Đổi refresh token lấy cặp token mới, ghi lại cookie.
 * Trả về access token mới (chỉ dùng trong server).
 */
export async function refreshAuthCookies(refreshToken: string) {
  const { payload } = await authApiRequests.sRefreshToken({ refreshToken });
  await setAuthCookies(payload.data.accessToken, payload.data.refreshToken);
  return payload.data.accessToken;
}

/**
 * Thực thi lời gọi backend cần xác thực từ Next API route, tự làm mới token khi hết hạn
 * hoặc khi cookie accessToken đã mất nhưng vẫn còn refreshToken.
 */
export async function forwardAuthenticatedRequest<T>(
  caller: (accessToken: string) => Promise<{ payload: T }>
): Promise<Response> {
  const store = await cookies();
  let accessToken = store.get(ACCESS_COOKIE)?.value;
  const refreshToken = store.get(REFRESH_COOKIE)?.value;

  if (!accessToken && refreshToken) {
    try {
      accessToken = await refreshAuthCookies(refreshToken);
    } catch {
      return Response.json(
        { message: "Phiên đăng nhập đã hết hạn" },
        { status: 401 }
      );
    }
  }

  if (!accessToken) {
    return Response.json({ message: "Chưa đăng nhập" }, { status: 401 });
  }

  try {
    const { payload } = await caller(accessToken);
    return Response.json(payload);
  } catch (error) {
    if (error instanceof HttpError && error.status === 401 && refreshToken) {
      try {
        const freshToken = await refreshAuthCookies(refreshToken);
        const { payload } = await caller(freshToken);
        return Response.json(payload);
      } catch (retryError) {
        if (retryError instanceof HttpError) {
          return Response.json(retryError.payload, {
            status: retryError.status,
          });
        }
        return Response.json(
          { message: "Phiên đăng nhập đã hết hạn" },
          { status: 401 }
        );
      }
    }

    if (error instanceof HttpError) {
      return Response.json(error.payload, { status: error.status });
    }
    return Response.json({ message: "Có lỗi xảy ra" }, { status: 500 });
  }
}
