import http from "@/lib/http";
import {
  OrderListResType,
  OrderResType,
  OrderType,
} from "@/schemaValidations/order.schema";
import { NormalType } from "@/schemaValidations/pagination.schema";

const orderApiRequests = {
  customerListOrder: () =>
    http.get<OrderListResType>(`customer/orders/history`),

  sCustomerOrder: (body: OrderType, accessToken: string) =>
    http.post<OrderResType>("customer/orders/create", body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),
  customerOrder: (body: OrderType) =>
    http.post<OrderResType>("api/order", body, {
      baseUrl: "",
    }),

  adminOrder: (page: number, size: number) =>
    http.get<OrderListResType>(`admin/orders?page=${page}&size=${size}`),

  adminOrderCustomer: (page: number, size: number, id: string) =>
    http.get<OrderListResType>(
      `admin/orders/customer/${id}?page=${page}&size=${size}`
    ),

  adminOrderDone: (page: number, size: number) =>
    http.get<OrderListResType>(
      `admin/orders/status/DONE?page=${page}&size=${size}`
    ),

  employeeOrder: (page: number, size: number) =>
    http.get<OrderListResType>(`employee/orders?page=${page}&size=${size}`),

  updateByEmployee: (body: number) =>
    http.post<NormalType>("api/order/e/update", body, {
      baseUrl: "",
    }),

  sUpdateByEmployee: (body: number, accessToken: string) =>
    http.put<NormalType>(`employee/${body}/status?status=DONE`, null, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  storeOwnerOrder: (page: number, size: number) =>
    http.get<OrderListResType>(
      `storeowner/manager/order?page=${page}&size=${size}`
    ),
};

export default orderApiRequests;
