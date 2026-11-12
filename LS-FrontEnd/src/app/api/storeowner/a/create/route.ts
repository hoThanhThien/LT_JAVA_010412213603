import { forwardAuthenticatedRequest } from "@/lib/server-auth";
import storeOwnerApiRequests from "@/apiRequests/storeowner";
import { StoreOwnerType } from "@/schemaValidations/storeowner.schema";

export async function POST(request: Request) {
  const body = (await request.json()) as StoreOwnerType;
  return forwardAuthenticatedRequest((token) =>
    storeOwnerApiRequests.sCreateByAdmin(body, token)
  );
}
