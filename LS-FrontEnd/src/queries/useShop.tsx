import { useQuery } from "@tanstack/react-query";
import shopApiRequests from "@/apiRequests/shop";

const SHOP_STALE_TIME = 5 * 60 * 1000;

export const useShops = (enabled = true) => {
  return useQuery({
    queryKey: ["all-shop"],
    queryFn: shopApiRequests.shop,
    enabled,
    staleTime: SHOP_STALE_TIME,
  });
};

export const useShopCategories = (shopId: number) => {
  return useQuery({
    queryKey: ["shop-categories", shopId],
    queryFn: () => shopApiRequests.categories(shopId),
    enabled: shopId > 0,
    staleTime: SHOP_STALE_TIME,
  });
};

export const useShopServices = (shopId: number, categoryId: number) => {
  return useQuery({
    queryKey: ["shop-services", shopId, categoryId],
    queryFn: () => shopApiRequests.services(shopId, categoryId),
    enabled: shopId > 0 && categoryId > 0,
    staleTime: SHOP_STALE_TIME,
  });
};
