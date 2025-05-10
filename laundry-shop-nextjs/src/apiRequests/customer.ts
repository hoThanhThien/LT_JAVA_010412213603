import http from "@/lib/http";
import { CustomerResType } from "@/schemaValidations/customer.schema";

const customerApiRequests = {
  admin: (page: number, size: number) =>
    http.get<CustomerResType>(`admin/customers?page=${page}&size=${size}`),
};

export default customerApiRequests;
