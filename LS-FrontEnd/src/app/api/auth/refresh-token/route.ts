import { cookies } from "next/headers";
import {
  REFRESH_COOKIE,
  RefreshAuthError,
  refreshAuthCookies,
  verifyCsrfOrigin,
} from "@/lib/server-auth";

export async function POST(request: Request) {
  const csrfError = verifyCsrfOrigin(request);
  if (csrfError) return csrfError;

  const cookieStore = await cookies();
  const refreshToken = cookieStore.get(REFRESH_COOKIE)?.value;
  if (!refreshToken) {
    return Response.json(
      {
        message: "Không tìm thấy refreshToken",
      },
      {
        status: 401,
      }
    );
  }

  try {
    await refreshAuthCookies(refreshToken);
    // Không trả token về client, token mới đã nằm trong cookie HttpOnly
    return Response.json({ message: "Làm mới token thành công" });
  } catch (error: unknown) {
    const status = error instanceof RefreshAuthError ? error.status : 401;
    const message =
      error instanceof Error ? error.message : "Có lỗi xảy ra";
    return Response.json(
      {
        message,
      },
      {
        status,
      }
    );
  }
}
