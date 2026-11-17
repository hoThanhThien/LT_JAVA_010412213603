import authApiRequests from "@/apiRequests/auth";
import { useMutation } from "@tanstack/react-query";

export const useLoginMutation = () => {
  return useMutation({
    mutationFn: authApiRequests.login,
  });
};

export const useGoogleLoginMutation = () => {
  return useMutation({
    mutationFn: authApiRequests.googleLogin,
  });
};

export const useRegisterMutation = () => {
  return useMutation({
    mutationFn: authApiRequests.register,
  });
};

export const useLogoutMutation = () => {
  return useMutation({
    mutationFn: authApiRequests.logout,
  });
};
