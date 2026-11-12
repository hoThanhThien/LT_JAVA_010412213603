import { cookies } from "next/headers";
import jwt from "jsonwebtoken";
import authApiRequests from "@/apiRequests/auth";

export const ACCESS_COOKIE = "accessToken";
export const REFRESH_COOKIE = "refreshToken";

const cookieOptions = (token: string) => {
  const decoded = jwt.decode(token) as { exp?: number } | null;
  return {
    path: "/",
    httpOnly: true,
    sameSite: "strict" as const,
    secure: true,
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
