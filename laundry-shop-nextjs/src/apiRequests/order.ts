import http from "@/lib/http";
import {
  OrderListResType,
  OrderResType,
  OrderType,
} from "@/schemaValidations/order.schema";

const orderApiRequests = {
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

  storeOwnerOrder: (page: number, size: number) =>
    http.get<OrderListResType>(
      `storeowner/manager/order?page=${page}&size=${size}`
    ),
};

export default orderApiRequests;
