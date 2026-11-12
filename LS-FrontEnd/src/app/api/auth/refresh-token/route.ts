import { cookies } from "next/headers";
import { REFRESH_COOKIE, refreshAuthCookies } from "@/lib/server-auth";

export async function POST() {
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
  } catch (error: any) {
    return Response.json(
      {
        message: error.message ?? "Có lỗi xảy ra",
      },
      {
        status: 401,
      }
    );
  }
}
