import http from "@/lib/http";
import {
  CategoryResType,
  ServiceResType,
  ShopListResType,
  ShopResType,
  ShopType,
  ShopUpdateType,
} from "@/schemaValidations/store.schema";

const shopApiRequests = {
  shop: () => http.get<ShopResType>("shops"),

  storeOwner: () =>
    http.get<{ data: ShopType; message: string }>("storeowner/shop"),

  admin: (page: number, size: number) =>
    http.get<ShopListResType>(`admin/shops?page=${page}&size=${size}`),

  categories: (id: number) =>
    http.get<CategoryResType>(`/shop/${id}/categories`),

  services: (idShop: number, idCategory: number) =>
    http.get<ServiceResType>(`/shop/${idShop}/category/${idCategory}/services`),

  update: (body: ShopType) =>
    http.put<ShopUpdateType>("storeowner/shop/update", body),
  create: (body: ShopType) =>
    http.post<ShopUpdateType>("storeowner/shop/create", body),
};

export default shopApiRequests;
