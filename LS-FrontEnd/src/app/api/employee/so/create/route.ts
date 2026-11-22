import { forwardAuthenticatedJson } from "@/lib/server-auth";
import { EmployeeType } from "@/schemaValidations/employee.schema";
import employeeApiRequests from "@/apiRequests/employee";

export async function POST(request: Request) {
  return forwardAuthenticatedJson<EmployeeType, unknown>(request, (body, token) =>
    employeeApiRequests.sCreateByStoreOwner(body, token)
  );
}
