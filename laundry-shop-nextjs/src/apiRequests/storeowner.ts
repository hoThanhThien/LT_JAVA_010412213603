import http from "@/lib/http";
import {
  StoreOwnerListResType,
  StoreOwnerResType,
  StoreOwnerType,
} from "@/schemaValidations/storeowner.schema";

const storeOwnerApiRequests = {
  admin: (page: number, size: number) =>
    http.get<StoreOwnerListResType>(
      `admin/storeowners?page=${page}&size=${size}`
    ),

  createByAdmin: (body: StoreOwnerType) =>
    http.post<StoreOwnerResType>(`api/storeowner/a/create`, body, {
      baseUrl: "",
    }),

  sCreateByAdmin: (body: StoreOwnerType, accessToken: string) =>
    http.post<StoreOwnerResType>(`admin/storeowner/create`, body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),

  updateByAdmin: (body: StoreOwnerType) =>
    http.post<StoreOwnerResType>("api/storeowner/a/update", body, {
      baseUrl: "",
    }),

  sUpdateByAdmin: (body: StoreOwnerType, accessToken: string) =>
    http.put<StoreOwnerResType>("admin/storeowner/update", body, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),
};

export default storeOwnerApiRequests;
