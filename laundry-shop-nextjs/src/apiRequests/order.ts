import http from "@/lib/http";
import { OrderResType } from "@/schemaValidations/order.schema";

const orderApiRequests = {
  adminOrder: (page: number, size: number) =>
    http.get<OrderResType>(`admin/orders?page=${page}&size=${size}`),
};

export default orderApiRequests;
