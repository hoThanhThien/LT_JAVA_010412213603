import { RoleValues } from "@/constants/type";
import z from "zod";

export const AccountSchema = z.object({
  id: z.number(),
  username: z.string(),
  phone: z.string(),
  role: z.string(),
  email: z.string().optional(),
  address: z.string().optional(),
  avtUser: z.string().optional(),
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
    phone: z.string().optional(),
    username: z.string().optional(),
    password: z.string().optional(),
    email: z.string().email().optional(),
    avt_user: z.string().optional(),
    address: z.string().optional(),
  })
  .strict();

export type UpdateMeType = z.TypeOf<typeof UpdateMeBody>;

export const UpdateMeResSchema = z
  .object({
    data: z.object({
      username: z.string(),
      password: z.string(),
      email: z.string().email().nullable(),
      phone: z.string(),
      address: z.string().nullable(),
      role: z.string(),
      avtUser: z.string().nullable(),
      createdAt: z.string().datetime(),
      updatedAt: z.string().datetime(),
    }),
    message: z.string(),
  })
  .strict();

export type UpdateMeResType = z.TypeOf<typeof UpdateMeResSchema>;
