import authApiRequests from "@/apiRequests/auth";
import { cookies } from "next/headers";
import {
  ACCESS_COOKIE,
  REFRESH_COOKIE,
  clearAuthCookies,
  clearRefreshCache,
  verifyCsrfOrigin,
} from "@/lib/server-auth";

export async function POST(request: Request) {
  const csrfError = verifyCsrfOrigin(request);
  if (csrfError) return csrfError;

  const cookieStore = await cookies();
  const accessToken = cookieStore.get(ACCESS_COOKIE)?.value;
  const refreshToken = cookieStore.get(REFRESH_COOKIE)?.value;
  clearRefreshCache(refreshToken);
  await clearAuthCookies();

  if (!accessToken || !refreshToken) {
    return Response.json(
      {
        message: "Không tìm thấy token",
      },
      { status: 401 }
    );
  }

  try {
    const result = await authApiRequests.sLogout({
      accessToken,
      refreshToken,
    });

    return Response.json(result.payload);
  } catch (error) {
    return Response.json(
      {
        message: "Lỗi khi gọi API đến server backend",
      },
      {
        status: 200,
      }
    );
  }
}
