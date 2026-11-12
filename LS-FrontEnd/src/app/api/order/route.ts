import { forwardAuthenticatedRequest } from "@/lib/server-auth";
import { OrderType } from "@/schemaValidations/order.schema";
import orderApiRequests from "@/apiRequests/order";

export async function POST(request: Request) {
  const body = (await request.json()) as OrderType;
  return forwardAuthenticatedRequest((token) =>
    orderApiRequests.sCustomerOrder(body, token)
  );
}
