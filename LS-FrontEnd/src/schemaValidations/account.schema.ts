import { RoleValues } from "@/constants/type";
import z from "zod";

export const AccountSchema = z.object({
  id: z.string(),
  username: z.string(),
  phone: z.string().nullable().optional(),
  role: z.string(),
  email: z.string().nullable().optional(),
  address: z.string().nullable().optional(),
  avtUser: z.string().nullable().optional(),
  createdAt: z.string().optional(),
  updatedAt: z.string().optional(),
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
    email: z.string().email().optional().or(z.literal("")),
    avtUser: z.string().optional(),
    address: z.string().optional(),
  })
  .strict();

export type UpdateMeType = z.TypeOf<typeof UpdateMeBody>;

export const UpdateMeResSchema = z
  .object({
    data: z
      .object({
        id: z.string().optional(),
        username: z.string(),
        password: z.string().nullable().optional(),
        email: z.string().email().nullable().optional(),
        phone: z.string().nullable().optional(),
        address: z.string().nullable().optional(),
        role: z.string().optional(),
        avtUser: z.string().nullable().optional(),
        createdAt: z.string().optional(),
        updatedAt: z.string().optional(),
      })
      .nullable(),
    message: z.string(),
  })
  .strict();

export type UpdateMeResType = z.TypeOf<typeof UpdateMeResSchema>;
