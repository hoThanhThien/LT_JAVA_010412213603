import { forwardAuthenticatedJson } from "@/lib/server-auth";
import orderApiRequests from "@/apiRequests/order";

export async function POST(request: Request) {
  return forwardAuthenticatedJson<number, unknown>(request, (body, token) =>
    orderApiRequests.sUpdateByEmployee(body, token)
  );
}
