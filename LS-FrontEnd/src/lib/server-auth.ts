import { cookies } from "next/headers";
import jwt from "jsonwebtoken";
import envConfig from "@/config";

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
  store.delete({ name: ACCESS_COOKIE, path: "/" });
  store.delete({ name: REFRESH_COOKIE, path: "/" });
}

const STATE_CHANGING_METHODS = new Set(["POST", "PUT", "PATCH", "DELETE"]);

export function verifyCsrfOrigin(request: Request): Response | null {
  if (!STATE_CHANGING_METHODS.has(request.method.toUpperCase())) {
    return null;
  }
  const origin = request.headers.get("origin");
  if (!origin) {
    return null;
  }
  try {
    const originHost = new URL(origin).host;
    const forwardedHost = request.headers
      .get("x-forwarded-host")
      ?.split(",")[0]
      ?.trim();
    const expectedHost =
      forwardedHost ||
      request.headers.get("host") ||
      new URL(request.url).host;
    if (originHost !== expectedHost) {
      return Response.json(
        { message: "Yêu cầu không hợp lệ (CSRF Origin mismatch)" },
        { status: 403 }
      );
    }
    return null;
  } catch {
    return Response.json(
      { message: "Origin header không hợp lệ" },
      { status: 403 }
    );
  }
}

type ServerHttpErrorLike = {
  status?: number;
  payload?: unknown;
};

function isHttpErrorLike(error: unknown): error is { status: number; payload: unknown } {
  return (
    typeof error === "object" &&
    error !== null &&
    typeof (error as ServerHttpErrorLike).status === "number" &&
    "payload" in error
  );
}

/**
 * Đổi refresh token lấy cặp token mới, ghi lại cookie.
 * Trả về access token mới (chỉ dùng trong server, không phụ thuộc client http/utils).
 */
export async function refreshAuthCookies(refreshToken: string) {
  const res = await fetch(`${envConfig.NEXT_PUBLIC_API_ENDPOINT}/auth/refresh-token`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${refreshToken}`,
    },
    body: JSON.stringify({ refreshToken }),
    cache: "no-store",
  });

  if (!res.ok) {
    throw new Error("Không thể làm mới phiên đăng nhập");
  }

  const body = (await res.json()) as {
    data?: { accessToken?: string; refreshToken?: string };
  };
  const accessToken = body?.data?.accessToken;
  const nextRefreshToken = body?.data?.refreshToken;
  if (!accessToken || !nextRefreshToken) {
    throw new Error("Phản hồi refresh token không hợp lệ");
  }

  await setAuthCookies(accessToken, nextRefreshToken);
  return accessToken;
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
    if (isHttpErrorLike(error) && error.status === 401 && refreshToken) {
      try {
        const freshToken = await refreshAuthCookies(refreshToken);
        const { payload } = await caller(freshToken);
        return Response.json(payload);
      } catch (retryError) {
        if (isHttpErrorLike(retryError)) {
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

    if (isHttpErrorLike(error)) {
      return Response.json(error.payload, { status: error.status });
    }
    return Response.json({ message: "Có lỗi xảy ra" }, { status: 500 });
  }
}
