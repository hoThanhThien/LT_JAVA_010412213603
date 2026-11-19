import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import orderApiRequests from "@/apiRequests/order";

export const useAdminOrder = (page: number, size: number) => {
  return useQuery({
    queryKey: ["order-admin", page, size],
    queryFn: () => orderApiRequests.adminOrder(page, size),
  });
};

export const useOrderMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: orderApiRequests.customerOrder,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["customer-orders"] });
      queryClient.invalidateQueries({ queryKey: ["order-admin"] });
    },
  });
};
