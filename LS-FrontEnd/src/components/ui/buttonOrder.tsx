"use client";

import { useAuthStore } from "@/lib/zustand";

export default function ButtonOrder(props: {
  className?: string;
  category?: string;
  service?: string;
  children: React.ReactNode;
}) {
  const { isAuth, setOpenAuth, setOpenOrder, setBookingPreset } =
    useAuthStore();
  return (
    <div
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
      {props.children}
    </div>
  );
}
