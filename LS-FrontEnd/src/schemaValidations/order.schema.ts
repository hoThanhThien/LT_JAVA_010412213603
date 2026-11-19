import z from "zod";
import { MetaSchema } from "./pagination.schema";

export const OrderStatus = z.enum(["IN_PROGRESS", "DONE"]);

export const OrderListSchema = z.object({
  id: z.number().int().positive(),
  username: z.string(),
  phone: z.string().nullable().optional(),
  email: z.string().email().nullable().optional(),
  address: z.string().nullable().optional(),
  totalAmount: z.number().nonnegative(),
  orderStatus: OrderStatus,
  imgProduct: z.string().nullable().optional(),
  laundryShopName: z.string(),
  serviceCategoryName: z.string(),
  serviceName: z.string(),
  servicePrice: z.number().nonnegative(),
  orderVolume: z.number().positive(),
  createdAt: z.string().datetime(),
  instructions: z.string().nullable().optional(),
  paymentStatus: z.string().optional(),
});

export type OrderListType = z.TypeOf<typeof OrderListSchema>;

export const OrderListResSchema = z
  .object({
    data: z.object({
      meta: MetaSchema,
      result: z.array(OrderListSchema),
    }),
    message: z.string(),
  })
  .strict();

export type OrderListResType = z.TypeOf<typeof OrderListResSchema>;

export const OrderSchema = z
  .object({
    laundryShop: z.object({
      id: z
        .number({ message: "Vui lòng chọn cửa hàng" })
        .int()
        .positive("Vui lòng chọn cửa hàng"),
    }),
    serviceCategory: z.object({
      id: z
        .number({ message: "Vui lòng chọn loại dịch vụ" })
        .int()
        .positive("Vui lòng chọn loại dịch vụ"),
    }),
    service: z.object({
      id: z
        .number({ message: "Vui lòng chọn dịch vụ" })
        .int()
        .positive("Vui lòng chọn dịch vụ"),
    }),
    orderVolume: z
      .number({ message: "Vui lòng nhập khối lượng" })
      .positive("Khối lượng phải lớn hơn 0"),
    phone: z
      .string()
      .trim()
      .regex(
        /^0\d{9}$/,
        "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0"
      ),
    address: z.string().trim().min(1, "Vui lòng nhập địa chỉ giao nhận"),
    imgProduct: z.string(),
    instructions: z.string(),
  })
  .strict();

export type OrderType = z.TypeOf<typeof OrderSchema>;

export const OrderResSchema = z
  .object({
    data: z.object({
      id: z.number(),
      username: z.string(),
      phone: z.string().nullable().optional(),
      email: z.string().email().nullable().optional(),
      address: z.string().nullable().optional(),
      totalAmount: z.number(),
      orderStatus: OrderStatus,
      imgProduct: z.string().nullable().optional(),
      laundryShopName: z.string(),
      serviceCategoryName: z.string(),
      serviceName: z.string(),
      servicePrice: z.number(),
      orderVolume: z.number(),
      createdAt: z.string(),
      instructions: z.string().nullable().optional(),
    }),
    message: z.string(),
  })
  .strict();

export type OrderResType = z.TypeOf<typeof OrderResSchema>;
