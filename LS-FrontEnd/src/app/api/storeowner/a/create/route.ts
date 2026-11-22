import { forwardAuthenticatedJson } from "@/lib/server-auth";
import storeOwnerApiRequests from "@/apiRequests/storeowner";
import { StoreOwnerType } from "@/schemaValidations/storeowner.schema";

export async function POST(request: Request) {
  return forwardAuthenticatedJson<StoreOwnerType, unknown>(request, (body, token) =>
    storeOwnerApiRequests.sCreateByAdmin(body, token)
  );
}
