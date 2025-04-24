import { OrderType } from "@/schemaValidations/order.schema";
import orderApiRequests from "@/apiRequests/order";
import { HttpError } from "@/lib/http";
import { cookies } from "next/headers";

export async function POST(request: Request) {
  const cookieStore = cookies();
  const accessToken = (await cookieStore).get("accessToken")?.value;
  const body = (await request.json()) as OrderType;
  // const body = {
  //   laundryShop: { id: "1" },
  //   serviceCategory: { id: "1" },
  //   service: { id: "1" },
  //   orderVolume: 11,
  //   imgProduct: "",
  //   instructions: "ấdasd",
  // };

  try {
    const { payload } = await orderApiRequests.sCustomerOrder(
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
