import { RoleValues } from "@/constants/type";
import z from "zod";
import { MetaSchema } from "./pagination.schema";

const CustomerBody = z.object({
  id: z.string(),
  username: z.string(),
  password: z.string().nullable(),
  email: z.string().email().optional(),
  phone: z.string(),
  address: z.string().optional(),
  role: z.enum(RoleValues),
  avtUser: z.string(),
  createdAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
});

export type CustomerType = z.TypeOf<typeof CustomerBody>;

export const CustomerResBody = z
  .object({
    data: z.object({
      meta: MetaSchema,
      result: z.array(CustomerBody),
    }),
    message: z.string(),
  })
  .strict();

export type CustomerResType = z.TypeOf<typeof CustomerResBody>;
