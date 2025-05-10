import { useQuery } from "@tanstack/react-query";
import shopApiRequests from "@/apiRequests/shop";
export const useShops = () => {
  return useQuery({
    queryKey: ["all-shop"],
    queryFn: shopApiRequests.shop,
  });
};
