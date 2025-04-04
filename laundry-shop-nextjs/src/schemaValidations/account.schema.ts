import { RoleValues } from "@/constants/type";
import z from "zod";

export const AccountSchema = z.object({
  id: z.number(),
  name: z.string(),
  phone: z.string(),
  role: z.string(),
  avatar: z.string().nullable(),
});

export type AccountType = z.TypeOf<typeof AccountSchema>;

export const AccountRes = z
  .object({
    data: AccountSchema,
    message: z.string(),
  })
  .strict();

export type AccountResType = z.TypeOf<typeof AccountRes>;
