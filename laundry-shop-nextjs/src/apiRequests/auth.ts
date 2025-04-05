import http from "@/lib/http";
import {
  LoginBodyType,
  AuthResType,
  RegisterBodyType,
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
};

export default authApiRequests;
