import { RoleValues } from "@/constants/type";
import z from "zod";
import { MetaSchema } from "./pagination.schema";

const StoreOwnerBody = z.object({
  id: z.string(),
  username: z.string(),
  password: z.string(),
  phone: z.string(),
  email: z.string().email().optional(),
  address: z.string().optional(),
  avtUser: z.string(),
  roles: z.enum(RoleValues),
  createdAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
  employees: z.any(),
});

export type StoreOwnerType = z.TypeOf<typeof StoreOwnerBody>;

export const StoreOwnerResBody = z
  .object({
    data: StoreOwnerBody,
    message: z.string(),
  })
  .strict();

export type StoreOwnerResType = z.TypeOf<typeof StoreOwnerResBody>;

export const StoreOwnerListResBody = z
  .object({
    data: z.object({
      meta: MetaSchema,
      result: z.array(StoreOwnerBody),
    }),
    message: z.string(),
  })
  .strict();

export type StoreOwnerListResType = z.TypeOf<typeof StoreOwnerListResBody>;
