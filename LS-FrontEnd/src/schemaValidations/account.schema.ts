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

import {
  STRONG_PASSWORD_MESSAGE,
  STRONG_PASSWORD_REGEX,
} from "@/schemaValidations/auth.schema";

export const ChangePasswordBody = z
  .object({
    currentPassword: z.string().optional(),
    newPassword: z.string().regex(STRONG_PASSWORD_REGEX, STRONG_PASSWORD_MESSAGE),
    confirmPassword: z.string().min(1, "Vui lòng xác nhận mật khẩu mới"),
  })
  .strict()
  .superRefine((data, ctx) => {
    if (data.newPassword !== data.confirmPassword) {
      ctx.addIssue({
        code: "custom",
        path: ["confirmPassword"],
        message: "Xác nhận mật khẩu mới không khớp",
      });
    }
  });

export type ChangePasswordBodyType = z.TypeOf<typeof ChangePasswordBody>;

export const ChangePasswordRes = z.object({
  message: z.string(),
  data: z.unknown().nullable().optional(),
});

export type ChangePasswordResType = z.TypeOf<typeof ChangePasswordRes>;

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
