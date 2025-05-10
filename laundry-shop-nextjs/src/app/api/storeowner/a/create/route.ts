import { HttpError } from "@/lib/http";
import { cookies } from "next/headers";
import storeOwnerApiRequests from "@/apiRequests/storeowner";
import { StoreOwnerType } from "@/schemaValidations/storeowner.schema";

export async function POST(request: Request) {
  const cookieStore = cookies();
  const accessToken = (await cookieStore).get("accessToken")?.value;
  const body = (await request.json()) as StoreOwnerType;

  try {
    const { payload } = await storeOwnerApiRequests.sCreateByAdmin(
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
