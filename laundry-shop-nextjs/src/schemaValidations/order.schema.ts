import z from "zod";
import { MetaSchema } from "./pagination.schema";

export const OrderStatus = z.enum(["IN_PROGRESS", "DONE"]);

export const OrderListSchema = z.object({
  id: z.number().int().positive(),
  username: z.string(),
  phone: z.string(),
  email: z.string().email(),
  address: z.string(),
  totalAmount: z.number().nonnegative(),
  orderStatus: OrderStatus,
  imgProduct: z.string().nullable(),
  laundryShopName: z.string(),
  serviceCategoryName: z.string(),
  serviceName: z.string(),
  servicePrice: z.number().nonnegative(),
  orderVolume: z.number().positive(),
  createdAt: z.string().datetime(),
  instructions: z.string(),
  paymentStatus: z.string(),
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
      id: z.any(),
    }),
    serviceCategory: z.object({
      id: z.any(),
    }),
    service: z.object({
      id: z.any(),
    }),
    orderVolume: z.number(),
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
      phone: z.string(),
      email: z.string().email(),
      address: z.string(),
      totalAmount: z.number(),
      orderStatus: OrderStatus,
      imgProduct: z.string().nullable(),
      laundryShopName: z.string(),
      serviceCategoryName: z.string(),
      serviceName: z.string(),
      servicePrice: z.number(),
      orderVolume: z.number(),
      createdAt: z.string(),
      instructions: z.string(),
    }),
    message: z.string(),
  })
  .strict();

export type OrderResType = z.TypeOf<typeof OrderResSchema>;
