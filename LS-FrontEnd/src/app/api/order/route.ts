import { forwardAuthenticatedJson } from "@/lib/server-auth";
import { OrderType } from "@/schemaValidations/order.schema";
import orderApiRequests from "@/apiRequests/order";

export async function POST(request: Request) {
  return forwardAuthenticatedJson<OrderType, unknown>(request, (body, token) =>
    orderApiRequests.sCustomerOrder(body, token)
  );
}
