import { RoleValues } from "@/constants/type";
import z from "zod";

export const AccountSchema = z.object({
  id: z.number(),
  username: z.string(),
  phone: z.string(),
  role: z.string(),
  email: z.string().optional(),
  address: z.string().optional(),
  avatar: z.string().optional(),
  createdAt: z.string(),
  updatedAt: z.string(),
});

export type AccountType = z.TypeOf<typeof AccountSchema>;

export const AccountRes = z
  .object({
    data: z.object({
      account: AccountSchema,
    }),
    message: z.string(),
  })
  .strict();

export type AccountResType = z.TypeOf<typeof AccountRes>;

export const UpdateMeBody = z
  .object({
    phone: z.string(),
    username: z.string().optional(),
    password: z.string().optional(),
    email: z.string().email().optional(),
    avtUser: z.string().optional(),
    address: z.string().optional(),
  })
  .strict();

export type UpdateMeType = z.TypeOf<typeof UpdateMeBody>;
