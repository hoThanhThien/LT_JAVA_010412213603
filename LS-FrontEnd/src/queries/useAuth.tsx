import authApiRequests from "@/apiRequests/auth";
import { useMutation, useQueryClient } from "@tanstack/react-query";

export const useLoginMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: authApiRequests.login,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["account-me"] });
    },
  });
};

export const useGoogleLoginMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: authApiRequests.googleLogin,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["account-me"] });
    },
  });
};

export const useRegisterMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: authApiRequests.register,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["account-me"] });
    },
  });
};

export const useLogoutMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: authApiRequests.logout,
    onSuccess: () => {
      queryClient.removeQueries({ queryKey: ["account-me"] });
    },
  });
};
