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

export const STRONG_PASSWORD_REGEX =
  /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@#$%^&+=!*._?-]).{8,100}$/;

export const STRONG_PASSWORD_MESSAGE =
  "Mật khẩu từ 8 ký tự trở lên, gồm chữ, số và ký tự đặc biệt (@#$%^&+=!*._?-)";

export const ForgotPasswordBody = z
  .object({
    identifier: z
      .string()
      .trim()
      .min(1, "Vui lòng nhập email hoặc số điện thoại"),
  })
  .strict();

export type ForgotPasswordBodyType = z.TypeOf<typeof ForgotPasswordBody>;

export const ForgotPasswordRes = z.object({
  message: z.string(),
  data: z
    .object({
      emailSent: z.boolean(),
      maskedEmail: z.string().nullable().optional(),
      expiresInSeconds: z.number().optional(),
      devOtp: z.string().nullable().optional(),
    })
    .nullable()
    .optional(),
});

export type ForgotPasswordResType = z.TypeOf<typeof ForgotPasswordRes>;

export const ResetPasswordBody = z
  .object({
    identifier: z
      .string()
      .trim()
      .min(1, "Vui lòng nhập email hoặc số điện thoại"),
    otp: z
      .string()
      .trim()
      .regex(/^\d{6}$/, "Mã OTP phải gồm đúng 6 chữ số"),
    newPassword: z.string().regex(STRONG_PASSWORD_REGEX, STRONG_PASSWORD_MESSAGE),
    confirmPassword: z.string().min(1, "Vui lòng xác nhận mật khẩu mới"),
  })
  .strict()
  .superRefine((data, ctx) => {
    if (data.newPassword !== data.confirmPassword) {
      ctx.addIssue({
        code: "custom",
        path: ["confirmPassword"],
        message: "Xác nhận mật khẩu không khớp",
      });
    }
  });

export type ResetPasswordBodyType = z.TypeOf<typeof ResetPasswordBody>;

export const ApiMessageRes = z.object({
  message: z.string(),
  data: z.unknown().nullable().optional(),
});

export type ApiMessageResType = z.TypeOf<typeof ApiMessageRes>;
