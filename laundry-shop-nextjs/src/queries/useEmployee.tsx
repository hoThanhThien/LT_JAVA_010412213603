import { useMutation } from "@tanstack/react-query";
import employeeApiRequests from "@/apiRequests/employee";

export const useUpdateEmployeeByStoreOwnerMutation = () => {
  return useMutation({
    mutationFn: employeeApiRequests.updateByStoreOwner,
  });
};
