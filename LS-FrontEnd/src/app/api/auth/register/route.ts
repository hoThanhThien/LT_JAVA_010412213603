import authApiRequests from "@/apiRequests/auth";
import { RegisterBodyType } from "@/schemaValidations/auth.schema";
import { HttpError } from "@/lib/http";
import { setAuthCookies } from "@/lib/server-auth";

export async function POST(request: Request) {
  const body = (await request.json()) as RegisterBodyType;
  try {
    const { payload } = await authApiRequests.sRegister(body);
    if (!payload?.data) {
      return Response.json(
        { message: payload?.message || "Đăng ký thất bại" },
        { status: 400 }
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
