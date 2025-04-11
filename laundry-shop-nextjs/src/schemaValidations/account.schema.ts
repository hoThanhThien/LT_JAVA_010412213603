import { RoleValues } from "@/constants/type";
import z from "zod";

export const AccountSchema = z.object({
  account: z.object({
    id: z.number(),
    username: z.string(),
    phone: z.string(),
    role: z.string(),
    email: z.string().optional(),
    address: z.string().optional(),
  }),
});

export type AccountType = z.TypeOf<typeof AccountSchema>;

export const AccountRes = z
  .object({
    data: AccountSchema,
    message: z.string(),
  })
  .strict();

export type AccountResType = z.TypeOf<typeof AccountRes>;
