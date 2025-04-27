import http from "@/lib/http";
import {
  CategoryManageType,
  CategoryUpdateType,
  DeleteCategoryType,
  DeleteServiceType,
  ServiceManageType,
  ServicesListResType,
  ServicesUpdateType,
} from "@/schemaValidations/category.schema";
import { NormalType } from "@/schemaValidations/pagination.schema";

const serviceApiRequests = {
  storeOwner: (page: number, size: number) =>
    http.get<ServicesListResType>(
      `storeowner/manager/service?page=${page}&size=${size}`
    ),

  updateService: (body: ServiceManageType) =>
    http.post<ServicesUpdateType>("api/service/so/update", body, {
      baseUrl: "",
    }),

  sUpdateService: (body: ServiceManageType, accessToken: string) =>
    http.post<ServicesUpdateType>("storeowner/service/update", body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  createService: (body: ServiceManageType) =>
    http.post<ServicesUpdateType>(`api/service/so/create`, body, {
      baseUrl: "",
    }),

  sCreateService: (body: ServiceManageType, accessToken: string) =>
    http.post<ServicesUpdateType>("storeowner/service/add", body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  updateCategory: (body: CategoryManageType) =>
    http.post<CategoryUpdateType>("api/category/so/update", body, {
      baseUrl: "",
    }),

  sUpdateCategory: (body: CategoryManageType, accessToken: string) =>
    http.put<CategoryUpdateType>("storeowner/category/update", body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  createCategory: (body: CategoryManageType) =>
    http.post<CategoryUpdateType>(`api/category/so/create`, body, {
      baseUrl: "",
    }),

  sCreateCategory: (body: CategoryManageType, accessToken: string) =>
    http.post<CategoryUpdateType>("storeowner/category/add", body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  deleteCategory: (body: DeleteCategoryType) =>
    http.delete<NormalType>(`storeowner/category/delete?id=${body}`),

  deleteService: (body: DeleteServiceType) =>
    http.delete<NormalType>(`storeowner/service/delete?id=${body}`),
};

export default serviceApiRequests;
