import { forwardAuthenticatedJson } from "@/lib/server-auth";
import { ServiceManageType } from "@/schemaValidations/category.schema";
import serviceApiRequests from "@/apiRequests/service";

export async function POST(request: Request) {
  return forwardAuthenticatedJson<ServiceManageType, unknown>(request, (body, token) =>
    serviceApiRequests.sUpdateService(body, token)
  );
}
