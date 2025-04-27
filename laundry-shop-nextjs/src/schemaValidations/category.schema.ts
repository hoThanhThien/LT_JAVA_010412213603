import z from "zod";
import { MetaSchema } from "./pagination.schema";

export const ServiceBody = z.object({
  id: z.number().int().positive().optional(),
  name: z.string(),
  imageDesc: z.string(),
  description: z.string(),
  estimatedTime: z.string(),
  price: z.number().positive(),
  categoryId: z.number().nullable(),
});

export type ServiceManageType = z.TypeOf<typeof ServiceBody>;

export const ServicesUpdateBody = z
  .object({
    data: ServiceBody,
    message: z.string(),
  })
  .strict();

export type ServicesUpdateType = z.TypeOf<typeof ServicesUpdateBody>;

export const CategoryBody = z.object({
  id: z.number().int().positive().optional(),
  name: z.string(),
  imageDesc: z.string(),
  description: z.string(),
  services: z.array(ServiceBody),
});

export type CategoryManageType = z.TypeOf<typeof CategoryBody>;

export const CategoryUpdateBody = z
  .object({
    data: CategoryBody,
    message: z.string(),
  })
  .strict();

export type CategoryUpdateType = z.TypeOf<typeof CategoryUpdateBody>;

export const ServicesListResBody = z
  .object({
    data: z.object({
      meta: MetaSchema,
      result: z.array(CategoryBody),
    }),
    message: z.string(),
  })
  .strict();

export type ServicesListResType = z.TypeOf<typeof ServicesListResBody>;

export const DeleteCategoryBody = z.number();

export type DeleteCategoryType = z.TypeOf<typeof DeleteCategoryBody>;

export const DeleteServiceBody = z.number();

export type DeleteServiceType = z.TypeOf<typeof DeleteServiceBody>;
