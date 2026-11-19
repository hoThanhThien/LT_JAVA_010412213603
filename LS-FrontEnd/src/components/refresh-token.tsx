"use client";

import { checkAndRefreshToken } from "@/lib/utils";
import { useAuthStore } from "@/lib/zustand";
import { usePathname } from "next/navigation";
import { useEffect } from "react";

const UNAUTHENTICATED_PATH = ["/logout", "/refresh-token"];

export default function RefreshToken() {
  const pathname = usePathname();
  const { isAuth, setIsAuth } = useAuthStore();

  useEffect(() => {
    if (UNAUTHENTICATED_PATH.includes(pathname)) return;

    let interval: ReturnType<typeof setInterval> | null = null;
    const handleRefreshError = () => {
      if (interval) {
        clearInterval(interval);
        interval = null;
      }
      setIsAuth(false);
    };

    void checkAndRefreshToken({ onError: handleRefreshError });

    const TIMEOUT = 10000;
    interval = setInterval(() => {
      void checkAndRefreshToken({ onError: handleRefreshError });
    }, TIMEOUT);

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [pathname, isAuth, setIsAuth]);
  return null;
}
