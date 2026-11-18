import http from "@/lib/http";
import {
  AccountResType,
  ChangePasswordBodyType,
  ChangePasswordResType,
  UpdateMeType,
} from "@/schemaValidations/account.schema";

const accountApiRequests = {
  me: () => http.get<AccountResType>("accounts/me"),
  sMe: (accessToken: string) =>
    http.get<AccountResType>("accounts/me", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  updateMe: (body: UpdateMeType) =>
    http.put<AccountResType>("/customer/update", body),
  changePassword: (body: ChangePasswordBodyType) =>
    http.post<ChangePasswordResType>("/accounts/change-password", body),
};

export default accountApiRequests;
