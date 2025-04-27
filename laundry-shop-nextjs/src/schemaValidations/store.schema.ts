import z from "zod";
import { MetaSchema } from "./pagination.schema";

export const ShopSchema = z.object({
  id: z.number(),
  name: z.string(),
  description: z.string(),
  address: z.string(),
  openingHours: z.string(),
  averageRating: z.number(),
  createdAt: z.string().nullable(),
  storeOwner: z.object({
    username: z.string(),
  }),
});

export type ShopType = z.infer<typeof ShopSchema>;

export const ShopResSchema = z.object({
  message: z.string(),
  data: z.array(ShopSchema),
});

export type ShopResType = z.infer<typeof ShopResSchema>;

export const ShopListResBody = z
  .object({
    data: z.object({
      meta: MetaSchema,
      result: z.array(ShopSchema),
    }),
    message: z.string(),
  })
  .strict();

export type ShopListResType = z.TypeOf<typeof ShopListResBody>;

export const CategorySchema = z.object({
  id: z.number(),
  name: z.string(),
  imageDesc: z.string(),
  description: z.string(),
});

export type CategoryType = z.infer<typeof CategorySchema>;

export const CategoryResSchema = z.object({
  message: z.string(),
  data: z.array(CategorySchema),
});

export type CategoryResType = z.infer<typeof CategoryResSchema>;

export const ServiceSchema = z.object({
  id: z.number(),
  name: z.string(),
  description: z.string(),
  estimatedTime: z.string(),
  imageDesc: z.string(),
  price: z.number(),
});

export type ServiceType = z.infer<typeof ServiceSchema>;

export const ServiceResSchema = z.object({
  message: z.string(),
  data: z.array(ServiceSchema),
});

export type ServiceResType = z.infer<typeof ServiceResSchema>;

export const ShopUpdateBody = z
  .object({
    data: ShopSchema,
    message: z.string(),
  })
  .strict();

export type ShopUpdateType = z.TypeOf<typeof ShopUpdateBody>;
