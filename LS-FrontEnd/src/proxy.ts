import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import jwt from "jsonwebtoken";

const privatePaths = ["/manage"];

// Phải trùng với jwt.secret của backend (đặt trong .env.local, chỉ dùng phía server)
const JWT_SECRET = process.env.JWT_SECRET;

/**
 * Xác thực chữ ký + hạn + issuer của access token (trước đây chỉ decode nên cookie giả vẫn qua được).
 * Trả về null nếu token thiếu/sai/hết hạn (hoặc thiếu JWT_SECRET: từ chối luôn cho an toàn).
 */
function verifyAccessToken(token: string | undefined): { role?: string } | null {
  if (!token) return null;
  if (!JWT_SECRET) {
    console.error("[proxy] Thiếu biến môi trường JWT_SECRET, từ chối mọi token");
    return null;
  }
  try {
    return jwt.verify(token, JWT_SECRET, {
      algorithms: ["HS256"],
      issuer: "LAUNDRY",
    }) as { role?: string };
  } catch {
    return null;
  }
}

function redirectHome(request: NextRequest, clearCookies = false) {
  const url = new URL("/", request.url);
  url.searchParams.set("login", "true");
  const response = NextResponse.redirect(url);
  if (clearCookies) {
    response.cookies.delete("accessToken");
    response.cookies.delete("refreshToken");
  }
  return response;
}

// This function can be marked `async` if using `await` inside
export function proxy(request: NextRequest) {
  const { pathname, searchParams } = request.nextUrl;
  const accessToken = request.cookies.get("accessToken")?.value;
  const refreshToken = request.cookies.get("refreshToken")?.value;
  const isPrivate = privatePaths.some((path) => pathname.startsWith(path));

  if (!isPrivate) return NextResponse.next();

  //Chưa đăng nhập, k cho vào private path => login
  if (!refreshToken) {
    const url = new URL("/", request.url);
    url.searchParams.set("login", "true");
    url.searchParams.set("redirect", pathname);
    return NextResponse.redirect(url);
  }

  const claims = verifyAccessToken(accessToken);

  //Đã đăng nhập, nhưng access token thiếu/hết hạn/không hợp lệ => refresh
  if (!claims) {
    // Chống vòng lặp: đã refresh rồi mà token vẫn không xác thực được
    // (VD JWT_SECRET frontend lệch backend) => coi như chưa đăng nhập
    const attempts = Number(searchParams.get("_rt") ?? 0);
    if (attempts >= 1) return redirectHome(request, true);

    const url = new URL("/refresh-token", request.url);
    url.searchParams.set("redirect", `${pathname}?_rt=${attempts + 1}`);
    return NextResponse.redirect(url);
  }

  if (pathname.startsWith("/manage/admin") && claims.role !== "Admin") {
    return NextResponse.redirect(new URL("/", request.url));
  }

  if (
    pathname.startsWith("/manage/storeowner") &&
    claims.role !== "StoreOwner"
  ) {
    return NextResponse.redirect(new URL("/", request.url));
  }

  if (pathname.startsWith("/manage/employee") && claims.role !== "Employee") {
    return NextResponse.redirect(new URL("/", request.url));
  }

  return NextResponse.next();
}

// See "Matching Paths" below to learn more
export const config = {
  matcher: ["/manage/:path*", "/login", "/"],
};
