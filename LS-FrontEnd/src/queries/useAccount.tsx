import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import accountApiRequests from "@/apiRequests/account";
export const useAccountMe = (enabled = true) => {
  return useQuery({
    queryKey: ["account-me"],
    queryFn: accountApiRequests.me,
    enabled,
  });
};

export const useUpdateMeMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: accountApiRequests.updateMe,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["account-me"] });
    },
  });
};
