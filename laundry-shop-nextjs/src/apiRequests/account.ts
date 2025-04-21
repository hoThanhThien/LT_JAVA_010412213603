import http from "@/lib/http";
import {
  AccountResType,
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
};

export default accountApiRequests;
