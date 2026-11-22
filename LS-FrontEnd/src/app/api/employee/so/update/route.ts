import { forwardAuthenticatedJson } from "@/lib/server-auth";
import { EmployeeListType } from "@/schemaValidations/employee.schema";
import employeeApiRequests from "@/apiRequests/employee";

export async function POST(request: Request) {
  return forwardAuthenticatedJson<EmployeeListType, unknown>(request, (body, token) =>
    employeeApiRequests.sUpdateByStoreOwner(body, token)
  );
}
