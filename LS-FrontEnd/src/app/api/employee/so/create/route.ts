import { forwardAuthenticatedRequest } from "@/lib/server-auth";
import { EmployeeType } from "@/schemaValidations/employee.schema";
import employeeApiRequests from "@/apiRequests/employee";

export async function POST(request: Request) {
  const body = (await request.json()) as EmployeeType;
  return forwardAuthenticatedRequest((token) =>
    employeeApiRequests.sCreateByStoreOwner(body, token)
  );
}
