import { cookies } from "next/headers";
import jwt from "jsonwebtoken";
import { ACCESS_COOKIE, REFRESH_COOKIE } from "@/lib/server-auth";

type Claims = {
  exp?: number;
  iat?: number;
  role?: string;
  tokenType?: string;
} | null;

/**
 * Trả về thông tin phiên (KHÔNG chứa token) để giao diện biết
 * đã đăng nhập chưa, role gì và khi nào cần refresh.
 */
export async function GET() {
  const store = await cookies();
  const accessToken = store.get(ACCESS_COOKIE)?.value;
  const refreshToken = store.get(REFRESH_COOKIE)?.value;

  const headers = { "Cache-Control": "no-store" };
  if (!refreshToken) {
    return Response.json({ authenticated: false }, { headers });
  }

  const access = accessToken ? (jwt.decode(accessToken) as Claims) : null;
  const refresh = jwt.decode(refreshToken) as Claims;

  if (refresh?.tokenType && refresh.tokenType !== "refresh") {
    return Response.json({ authenticated: false }, { headers });
  }

  const validAccess =
    access && (!access.tokenType || access.tokenType === "access")
      ? access
      : null;

  return Response.json(
    {
      authenticated: true,
      role: validAccess?.role ?? refresh?.role ?? null,
      accessExp: validAccess?.exp ?? 0,
      accessIat: validAccess?.iat ?? 0,
      refreshExp: refresh?.exp ?? 0,
    },
    { headers }
  );
}

