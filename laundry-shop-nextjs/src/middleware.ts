import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import jwt from "jsonwebtoken";

const privatePaths = ["/manage"];

// This function can be marked `async` if using `await` inside
export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;
  const accessToken = request.cookies.get("accessToken")?.value;
  const refreshToken = request.cookies.get("refreshToken")?.value;

  const decodedAccessToken = jwt.decode(accessToken!) as { role: string };

  //Chưa đăng nhập, k cho vào private path => login
  if (privatePaths.some((path) => pathname.startsWith(path)) && !refreshToken) {
    const url = new URL("/", request.url);
    url.searchParams.set("login", "true");
    url.searchParams.set("redirect", pathname);
    return NextResponse.redirect(url);
  }
  // // //Đã đăng nhập => không vào dc /login
  // if (unAuthPaths.some((path) => pathname.startsWith(path)) && refreshToken) {
  //   return NextResponse.redirect(new URL("/", request.url));
  // }
  //Đã đăng nhập, nhưng accessToken hết hạn => logout
  if (
    privatePaths.some((path) => pathname.startsWith(path)) &&
    !accessToken &&
    refreshToken
  ) {
    const url = new URL("/refresh-token", request.url);
    url.searchParams.set("refreshToken", refreshToken);
    url.searchParams.set("redirect", pathname);
    return NextResponse.redirect(url);
  }

  if (
    pathname.startsWith("/manage/admin") &&
    decodedAccessToken.role !== "Admin"
  ) {
    const url = new URL("/", request.url);
    return NextResponse.redirect(url);
  }

  if (
    pathname.startsWith("/manage/storeowner") &&
    decodedAccessToken.role !== "StoreOwner"
  ) {
    const url = new URL("/", request.url);
    return NextResponse.redirect(url);
  }

  if (
    pathname.startsWith("/manage/employee") &&
    decodedAccessToken.role !== "Employee"
  ) {
    const url = new URL("/", request.url);
    return NextResponse.redirect(url);
  }

  return NextResponse.next();
}

// See "Matching Paths" below to learn more
export const config = {
  matcher: ["/manage/:path*", "/login", "/"],
};
