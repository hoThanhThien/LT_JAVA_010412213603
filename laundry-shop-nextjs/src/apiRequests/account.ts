import http from "@/lib/http";
import { AccountResType } from "@/schemaValidations/account.schema";

const accountApiRequests = {
  me: () => http.get<AccountResType>("auth/accounts/me"),
};

export default accountApiRequests;
