import http from "@/lib/http";
import {
  PaymentResType,
  PaymentType,
} from "@/schemaValidations/payment.schema";

const paymentApiRequests = {
  get: (id: number) => http.get<PaymentType>(`/api/payments/info/${id}`),

  status: (id: number) =>
    http.get<PaymentResType>(`/api/payments/status/${id}`),
};

export default paymentApiRequests;
