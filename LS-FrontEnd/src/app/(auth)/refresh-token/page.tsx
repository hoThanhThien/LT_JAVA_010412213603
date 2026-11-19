"use client";
import Loading from "@/components/loading";
import authApiRequests from "@/apiRequests/auth";
import { resetSessionCache } from "@/lib/utils";
import { useAuthStore } from "@/lib/zustand";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect, useRef, Suspense } from "react";

function RefreshTokenHandler() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { setIsAuth } = useAuthStore();
  const redirectParam = searchParams.get("redirect");
  const started = useRef(false);

  // Chỉ cho phép chuyển hướng nội bộ
  const redirectPathname =
    redirectParam && redirectParam.startsWith("/") && !redirectParam.startsWith("//")
      ? redirectParam
      : "/";

  useEffect(() => {
    if (started.current) return;
    started.current = true;

    // Refresh token nằm trong cookie HttpOnly, server Next tự đọc và làm mới
    authApiRequests
      .refreshToken()
      .then(() => {
        resetSessionCache();
        setIsAuth(true);
        router.push(redirectPathname);
      })
      .catch(() => {
        resetSessionCache();
        setIsAuth(false);
        router.push("/");
      });
  }, [router, redirectPathname, setIsAuth]);

  return <Loading />;
}

export default function RefreshTokenPage() {
  return (
    <Suspense fallback={<Loading />}>
      <RefreshTokenHandler />
    </Suspense>
  );
}
