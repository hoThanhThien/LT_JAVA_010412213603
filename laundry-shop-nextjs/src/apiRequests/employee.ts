import http from "@/lib/http";
import {
  DeleteEmployeeResType,
  DeleteEmployeeType,
  EmployeeListResType,
  EmployeeType,
  UpdateEmployeeResType,
  UpdateEmployeeType,
} from "@/schemaValidations/employee.schema";

const employeeApiRequests = {
  storeOwner: (page: number, size: number) =>
    http.get<EmployeeListResType>(
      `storeowner/employees?page=${page}&size=${size}`
    ),

  admin: (page: number, size: number) =>
    http.get<EmployeeListResType>(`/admin/employees?page=${page}&size=${size}`),

  sUpdateByStoreOwner: (body: UpdateEmployeeType, accessToken: string) =>
    http.put<UpdateEmployeeResType>("storeowner/employee/update", body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  updateByStoreOwner: (body: UpdateEmployeeType) =>
    http.post<UpdateEmployeeResType>("api/employee/so/update", body, {
      baseUrl: "",
    }),

  sUpdateByAdmin: (body: UpdateEmployeeType, accessToken: string) =>
    http.put<UpdateEmployeeResType>("storeowner/employee/update", body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  deleteByStoreOwner: (body: DeleteEmployeeType) =>
    http.delete<DeleteEmployeeResType>(
      `storeowner/employee/delete?phone=${body}`
    ),

  createByStoreOwner: (body: EmployeeType) =>
    http.post<UpdateEmployeeResType>(`api/employee/so/create`, body, {
      baseUrl: "",
    }),

  sCreateByStoreOwner: (body: EmployeeType, accessToken: string) =>
    http.post<UpdateEmployeeResType>(`storeowner/employee/create`, body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  updateByAdmin: (body: UpdateEmployeeType) =>
    http.post<UpdateEmployeeResType>("api/employee/a/update", body, {
      baseUrl: "",
    }),
};

export default employeeApiRequests;
