import { RoleValues } from "@/constants/type";
import z from "zod";

export const LoginBody = z
  .object({
    phone: z.string().min(10).max(15),
    password: z.string().min(3).max(100),
  })
  .strict();

export type LoginBodyType = z.TypeOf<typeof LoginBody>;

export const AuthRes = z.object({
  data: z.object({
    accessToken: z.string(),
    refreshToken: z.string(),
    account: z.object({
      id: z.string(),
      name: z.string(),
      phone: z.string().nullable().optional(),
      email: z.string().nullable().optional(),
      address: z.string().nullable().optional(),
      role: z.enum(RoleValues),
    }),
  }),
  message: z.string(),
});

export type AuthResType = z.TypeOf<typeof AuthRes>;

// Phản hồi từ Next server cho trình duyệt: KHÔNG chứa token (token nằm trong cookie HttpOnly)
export const ClientAuthRes = z.object({
  data: z.object({ account: AuthRes.shape.data.shape.account }),
  message: z.string(),
});

export type ClientAuthResType = z.TypeOf<typeof ClientAuthRes>;

export type SessionResType = {
  authenticated: boolean;
  role?: string | null;
  accessExp?: number;
  accessIat?: number;
  refreshExp?: number;
};

export const RegisterBody = z
  .object({
    username: z.string().min(3).max(100),
    phone: z.string().min(10).max(15),
    password: z.string().min(3).max(100),
    idToken: z.string().optional(),
  })
  .strict();

export type RegisterBodyType = z.TypeOf<typeof RegisterBody>;

export const GoogleLoginBody = z
  .object({
    idToken: z.string().min(1),
    googleIdToken: z.string().nullable().optional(),
  })
  .strict();

export type GoogleLoginBodyType = z.TypeOf<typeof GoogleLoginBody>;

export const RefreshTokenBody = z
  .object({
    refreshToken: z.string(),
  })
  .strict();

export type RefreshTokenBodyType = z.TypeOf<typeof RefreshTokenBody>;

export const RefreshTokenRes = z.object({
  data: z.object({
    accessToken: z.string(),
    refreshToken: z.string(),
  }),
  message: z.string(),
});

export type RefreshTokenResType = z.TypeOf<typeof RefreshTokenRes>;

export const LogoutBody = z
  .object({
    accessToken: z.string(),
    refreshToken: z.string(),
  })
  .strict();

export type LogoutBodyType = z.TypeOf<typeof LogoutBody>;
