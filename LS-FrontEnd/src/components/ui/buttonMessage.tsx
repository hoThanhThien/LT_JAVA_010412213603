"use client";

import { useAuthStore } from "@/lib/zustand";

export default function ButtonMessage(props: {
  className?: string;
  category?: string;
  service?: string;
  label?: string;
}) {
  const { isAuth, setOpenAuth, setOpenOrder, setBookingPreset } =
    useAuthStore();

  return (
    <button
      type="button"
      className={props.className}
      onClick={() => {
        if (props.category !== undefined || props.service !== undefined) {
          setBookingPreset({
            category: props.category ?? "",
            service: props.service ?? "",
          });
        }
        if (!isAuth) {
          setOpenAuth(true);
        } else {
          setOpenOrder(true);
        }
      }}
    >
      {props.label ?? "ĐẶT DỊCH VỤ NGAY"}
    </button>
  );
}
