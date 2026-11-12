import { useMutation, useQuery } from "@tanstack/react-query";
import accountApiRequests from "@/apiRequests/account";
export const useAccountMe = (enabled = true) => {
  return useQuery({
    queryKey: ["account-me"],
    queryFn: accountApiRequests.me,
    enabled,
  });
};

export const useUpdateMeMutation = () => {
  return useMutation({
    mutationFn: accountApiRequests.updateMe,
  });
};
