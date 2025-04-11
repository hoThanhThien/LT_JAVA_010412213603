import http from "@/lib/http";
import {
  LoginBodyType,
  AuthResType,
  RegisterBodyType,
  LogoutBodyType,
} from "@/schemaValidations/auth.schema";

const authApiRequests = {
  sLogin: (body: LoginBodyType) => http.post<AuthResType>("/auth/login", body),
  login: (body: LoginBodyType) =>
    http.post<AuthResType>("api/auth/login", body, {
      baseUrl: "",
    }),

  sRegister: (body: RegisterBodyType) =>
    http.post<AuthResType>("/auth/register", body),
  register: (body: RegisterBodyType) =>
    http.post<AuthResType>("api/auth/register", body, {
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
};

export default authApiRequests;
