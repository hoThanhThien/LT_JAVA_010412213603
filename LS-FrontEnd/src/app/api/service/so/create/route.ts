import { forwardAuthenticatedRequest } from "@/lib/server-auth";
import { ServiceManageType } from "@/schemaValidations/category.schema";
import serviceApiRequests from "@/apiRequests/service";

export async function POST(request: Request) {
  const body = (await request.json()) as ServiceManageType;
  return forwardAuthenticatedRequest((token) =>
    serviceApiRequests.sCreateService(body, token)
  );
}
