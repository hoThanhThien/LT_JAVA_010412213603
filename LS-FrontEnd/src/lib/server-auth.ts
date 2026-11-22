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
  const sourceHeader = request.headers.get("origin") || request.headers.get("referer");
  if (!sourceHeader) {
    return null;
  }
  try {
    const originHost = new URL(sourceHeader).host;
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

export async function forwardAuthenticatedJson<TBody, TRes>(
  request: Request,
  caller: (body: TBody, accessToken: string) => Promise<{ payload: TRes }>
): Promise<Response> {
  const csrfError = verifyCsrfOrigin(request);
  if (csrfError) return csrfError;
  const body = (await request.json()) as TBody;
  return forwardAuthenticatedRequest((token) => caller(body, token));
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

type TokenPair = {
  accessToken: string;
  refreshToken: string;
};

export class RefreshAuthError extends Error {
  status: number;
  shouldClearCookies: boolean;

  constructor(message: string, status: number, shouldClearCookies = false) {
    super(message);
    this.status = status;
    this.shouldClearCookies = shouldClearCookies;
  }
}

const RECENT_REFRESH_TTL_MS = 3000;
const inflightRefreshes = new Map<string, Promise<TokenPair>>();
const recentRefreshes = new Map<string, { pair: TokenPair; expiresAt: number }>();

export function clearRefreshCache(refreshToken?: string) {
  if (!refreshToken) return;
  recentRefreshes.delete(refreshToken);
}

function pruneExpiredRecentRefreshes(now: number) {
  if (recentRefreshes.size <= 50) return;
  for (const [key, entry] of recentRefreshes.entries()) {
    if (entry.expiresAt <= now) {
      recentRefreshes.delete(key);
    }
  }
}

async function fetchNewTokenPair(refreshToken: string): Promise<TokenPair> {
  let res: Response;
  try {
    res = await fetch(`${envConfig.NEXT_PUBLIC_API_ENDPOINT}/auth/refresh-token`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${refreshToken}`,
      },
      body: JSON.stringify({ refreshToken }),
      cache: "no-store",
    });
  } catch {
    throw new RefreshAuthError("Không thể kết nối tới máy chủ xác thực", 502, false);
  }

  if (!res.ok) {
    const isRejected = res.status === 401 || res.status === 403;
    throw new RefreshAuthError(
      "Không thể làm mới phiên đăng nhập",
      isRejected ? 401 : res.status,
      isRejected
    );
  }

  const body = (await res.json()) as {
    data?: { accessToken?: string; refreshToken?: string };
  };
  const accessToken = body?.data?.accessToken;
  const nextRefreshToken = body?.data?.refreshToken;
  if (!accessToken || !nextRefreshToken) {
    throw new RefreshAuthError("Phản hồi refresh token không hợp lệ", 401, true);
  }

  return { accessToken, refreshToken: nextRefreshToken };
}

/**
 * Đổi refresh token lấy cặp token mới, ghi lại cookie.
 * Gộp các request song song cùng một refreshToken để tránh Token Rotation tự hủy phiên.
 */
export async function refreshAuthCookies(refreshToken: string) {
  const now = Date.now();
  const recent = recentRefreshes.get(refreshToken);
  if (recent && recent.expiresAt > now) {
    await setAuthCookies(recent.pair.accessToken, recent.pair.refreshToken);
    return recent.pair.accessToken;
  }

  let promise = inflightRefreshes.get(refreshToken);
  if (!promise) {
    promise = fetchNewTokenPair(refreshToken).finally(() => {
      inflightRefreshes.delete(refreshToken);
    });
    inflightRefreshes.set(refreshToken, promise);
  }

  try {
    const pair = await promise;
    pruneExpiredRecentRefreshes(Date.now());
    recentRefreshes.set(refreshToken, {
      pair,
      expiresAt: Date.now() + RECENT_REFRESH_TTL_MS,
    });
    await setAuthCookies(pair.accessToken, pair.refreshToken);
    return pair.accessToken;
  } catch (error) {
    if (error instanceof RefreshAuthError && error.shouldClearCookies) {
      recentRefreshes.delete(refreshToken);
      await clearAuthCookies();
    }
    throw error;
  }
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
    } catch (err) {
      const status = err instanceof RefreshAuthError ? err.status : 401;
      const message =
        status >= 500
          ? "Không kết nối được tới server backend"
          : "Phiên đăng nhập đã hết hạn";
      return Response.json({ message }, { status });
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
        if (retryError instanceof RefreshAuthError) {
          const message =
            retryError.status >= 500
              ? "Không kết nối được tới server backend"
              : "Phiên đăng nhập đã hết hạn";
          return Response.json({ message }, { status: retryError.status });
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
