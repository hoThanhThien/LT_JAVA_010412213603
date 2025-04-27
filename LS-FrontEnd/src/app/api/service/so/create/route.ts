import { HttpError } from "@/lib/http";
import { cookies } from "next/headers";
import { ServiceManageType } from "@/schemaValidations/category.schema";
import serviceApiRequests from "@/apiRequests/service";

export async function POST(request: Request) {
  const cookieStore = cookies();
  const accessToken = (await cookieStore).get("accessToken")?.value;
  const body = (await request.json()) as ServiceManageType;

  try {
    const { payload } = await serviceApiRequests.sCreateService(
      body,
      accessToken!
    );

    return Response.json(payload);
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
