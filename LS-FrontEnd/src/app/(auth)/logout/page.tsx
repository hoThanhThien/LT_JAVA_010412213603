"use client";

import Loading from "@/components/loading";
import { resetSessionCache } from "@/lib/utils";
import { useAuthStore } from "@/lib/zustand";
import { useLogoutMutation } from "@/queries/useAuth";
import { useRouter } from "next/navigation";
import { useEffect, useRef, Suspense } from "react";

function LogoutHandler() {
  const { mutateAsync } = useLogoutMutation();
  const { setIsAuth } = useAuthStore();
  const router = useRouter();
  const started = useRef(false);

  useEffect(() => {
    if (started.current) return;
    started.current = true;

    // Server Next đọc token từ cookie HttpOnly, gọi backend logout rồi xóa cookie
    mutateAsync()
      .catch(() => {})
      .finally(() => {
        resetSessionCache();
        setIsAuth(false);
        router.push("/");
      });
  }, [mutateAsync, router, setIsAuth]);
  return <Loading />;
}

export default function Logout() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <LogoutHandler />
    </Suspense>
  );
}
