import { forwardAuthenticatedJson } from "@/lib/server-auth";
import { CategoryManageType } from "@/schemaValidations/category.schema";
import serviceApiRequests from "@/apiRequests/service";

export async function POST(request: Request) {
  return forwardAuthenticatedJson<CategoryManageType, unknown>(request, (body, token) =>
    serviceApiRequests.sCreateCategory(body, token)
  );
}
