import http from "@/lib/http";
import {
  LoginBodyType,
  GoogleLoginBodyType,
  AuthResType,
  ClientAuthResType,
  SessionResType,
  RegisterBodyType,
  LogoutBodyType,
  ApiMessageResType,
  ForgotPasswordBodyType,
  ForgotPasswordResType,
  RefreshTokenBodyType,
  ResetPasswordBodyType,
  RefreshTokenResType,
} from "@/schemaValidations/auth.schema";

const authApiRequests = {
  sLogin: (body: LoginBodyType) => http.post<AuthResType>("/auth/login", body),
  login: (body: LoginBodyType) =>
    http.post<ClientAuthResType>("api/auth/login", body, {
      baseUrl: "",
    }),

  sGoogleLogin: (body: GoogleLoginBodyType) =>
    http.post<AuthResType>("/auth/google", body),
  googleLogin: (body: GoogleLoginBodyType) =>
    http.post<ClientAuthResType>("api/auth/google", body, {
      baseUrl: "",
    }),

  sRegister: (body: RegisterBodyType) =>
    http.post<AuthResType>("/auth/register", body),
  register: (body: RegisterBodyType) =>
    http.post<ClientAuthResType>("api/auth/register", body, {
      baseUrl: "",
    }),

  sLogout: (body: LogoutBodyType) =>
    http.post(
      "/auth/logout",
      { accessToken: body.accessToken, refreshToken: body.refreshToken },
      {
        headers: {
          Authorization: `Bearer ${body.accessToken}`,
        },
      }
    ),
  logout: () =>
    http.post("api/auth/logout", null, {
      baseUrl: "",
    }),

  session: () =>
    http.get<SessionResType>("api/auth/session", {
      baseUrl: "",
      cache: "no-store",
    }),

  refreshTokenRequest: null as Promise<{
    status: number;
    payload: { message: string };
  }> | null,

  async refreshToken() {
    if (this.refreshTokenRequest) return this.refreshTokenRequest;
    this.refreshTokenRequest = http.post<{ message: string }>(
      "api/auth/refresh-token",
      null,
      {
        baseUrl: "",
      }
    );

    const result = await this.refreshTokenRequest;
    this.refreshTokenRequest = null;
    return result;
  },
  sRefreshToken: (body: RefreshTokenBodyType) =>
    http.post<RefreshTokenResType>("auth/refresh-token", body, {
      headers: {
        Authorization: `Bearer ${body.refreshToken}`,
      },
    }),
  forgotPassword: (body: ForgotPasswordBodyType) =>
    http.post<ForgotPasswordResType>("auth/forgot-password", body),
  resetPassword: (body: ResetPasswordBodyType) =>
    http.post<ApiMessageResType>("auth/reset-password", body),
};

export default authApiRequests;
