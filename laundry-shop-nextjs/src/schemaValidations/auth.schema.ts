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
      id: z.number(),
      name: z.string(),
      phone: z.string(),
      email: z.string().optional(),
      address: z.string().optional(),
      role: z.enum(RoleValues),
    }),
  }),
  message: z.string(),
});

export type AuthResType = z.TypeOf<typeof AuthRes>;

export const RegisterBody = z
  .object({
    username: z.string().min(3).max(100),
    phone: z.string().min(10).max(15),
    password: z.string().min(3).max(100),
    idToken: z.string().optional(),
  })
  .strict();

export type RegisterBodyType = z.TypeOf<typeof RegisterBody>;

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
