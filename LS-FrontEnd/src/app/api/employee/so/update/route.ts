import { forwardAuthenticatedRequest } from "@/lib/server-auth";
import { EmployeeListType } from "@/schemaValidations/employee.schema";
import employeeApiRequests from "@/apiRequests/employee";

export async function POST(request: Request) {
  const body = (await request.json()) as EmployeeListType;
  return forwardAuthenticatedRequest((token) =>
    employeeApiRequests.sUpdateByStoreOwner(body, token)
  );
}
