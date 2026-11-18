import authApiRequests from "@/apiRequests/auth";
import { LoginBodyType } from "@/schemaValidations/auth.schema";
import { HttpError } from "@/lib/http";
import { setAuthCookies, verifyCsrfOrigin } from "@/lib/server-auth";

export async function POST(request: Request) {
  const csrfError = verifyCsrfOrigin(request);
  if (csrfError) return csrfError;

  const body = (await request.json()) as LoginBodyType;
  try {
    const { payload } = await authApiRequests.sLogin(body);
    if (!payload?.data) {
      return Response.json(
        { message: payload?.message || "Đăng nhập thất bại" },
        { status: 401 }
      );
    }
    const { accessToken, refreshToken, account } = payload.data;

    // Token chỉ nằm trong cookie HttpOnly, không trả về cho trình duyệt
    await setAuthCookies(accessToken, refreshToken);
    return Response.json({ message: payload.message, data: { account } });
  } catch (error) {
    if (error instanceof HttpError) {
      return Response.json(error.payload, {
        status: error.status,
      });
    } else {
      return Response.json(
        {
          message: "Có lỗi xảy ra",
        },
        {
          status: 500,
        }
      );
    }
  }
}
