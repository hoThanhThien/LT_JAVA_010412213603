import { useMutation, useQuery } from "@tanstack/react-query";
import orderApiRequests from "@/apiRequests/order";

export const useAdminOrder = (page: number, size: number) => {
  return useQuery({
    queryKey: ["order-admin"],
    queryFn: () => orderApiRequests.adminOrder(page, size),
  });
};

export const useOrderMutation = () => {
  return useMutation({
    mutationFn: orderApiRequests.customerOrder,
  });
};
