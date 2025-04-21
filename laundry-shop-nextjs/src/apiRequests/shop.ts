import http from "@/lib/http";
import {
  CategoryResType,
  ServiceResType,
  ShopResType,
} from "@/schemaValidations/shop.schema";

const shopApiRequests = {
  shop: () => http.get<ShopResType>("shops"),

  categories: (id: number) =>
    http.get<CategoryResType>(`/shop/${id}/categories`),

  services: (idShop: number, idCategory: number) =>
    http.get<ServiceResType>(`/shop/${idShop}/category/${idCategory}/services`),
};

export default shopApiRequests;
