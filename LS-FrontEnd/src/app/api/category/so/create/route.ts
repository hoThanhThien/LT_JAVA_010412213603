import { forwardAuthenticatedRequest } from "@/lib/server-auth";
import { CategoryManageType } from "@/schemaValidations/category.schema";
import serviceApiRequests from "@/apiRequests/service";

export async function POST(request: Request) {
  const body = (await request.json()) as CategoryManageType;
  return forwardAuthenticatedRequest((token) =>
    serviceApiRequests.sCreateCategory(body, token)
  );
}
