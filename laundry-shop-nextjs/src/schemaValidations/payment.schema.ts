import { z } from "zod";

// Định nghĩa schema cho dữ liệu thanh toán
export const PaymentSchema = z.object({
  orderId: z.number(),
  totalAmount: z.number(),
  accountHolder: z.string(),
  bankName: z.string(),
  accountNumber: z.string(),
  transferContent: z.string(),
  paymentStatus: z.enum(["PAID", "PENDING"]), // Giả sử các trạng thái có thể
  qrCode: z.string().url(),
});

// Type được suy ra từ schema
export type PaymentType = z.infer<typeof PaymentSchema>;

export const PaymentDataSchema = z.object({
  totalAmount: z.number(),
  createdAt: z.string().datetime(), // Xác nhận là chuỗi định dạng datetime
  orderId: z.number().int().positive(),
  orderStatus: z.enum(["IN_PROGRESS", "DONE"]), // Các trạng thái có thể có
  paymentStatus: z.enum(["PAID", "PENDING"]), // Các trạng thái thanh toán có thể có
});

// Schema cho toàn bộ response
export const PaymentResSchema = z.object({
  message: z.string(),
  data: PaymentDataSchema,
});

export type PaymentResType = z.infer<typeof PaymentResSchema>;
