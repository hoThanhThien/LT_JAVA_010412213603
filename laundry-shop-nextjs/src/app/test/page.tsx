"use client";

import { useAdminOrder } from "@/queries/useOrder";

export default function page() {
  const { data } = useAdminOrder(1, 10);
  console.log(data?.payload);
  return <div>Test page</div>;
}
