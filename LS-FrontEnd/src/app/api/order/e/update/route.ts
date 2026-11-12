import { forwardAuthenticatedRequest } from "@/lib/server-auth";
import orderApiRequests from "@/apiRequests/order";

export async function POST(request: Request) {
  const body = (await request.json()) as number;
  return forwardAuthenticatedRequest((token) =>
    orderApiRequests.sUpdateByEmployee(body, token)
  );
}
