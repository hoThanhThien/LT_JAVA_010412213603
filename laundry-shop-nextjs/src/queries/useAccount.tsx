import { useQuery } from "@tanstack/react-query";
import accountApiRequests from "@/apiRequests/account";
export const useAccountMe = () => {
  return useQuery({
    queryKey: ["account-me"],
    queryFn: accountApiRequests.me,
  });
};
