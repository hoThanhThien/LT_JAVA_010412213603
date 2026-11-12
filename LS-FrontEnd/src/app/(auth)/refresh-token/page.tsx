"use client";
import Loading from "@/components/loading";
import authApiRequests from "@/apiRequests/auth";
import { resetSessionCache } from "@/lib/utils";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect, useRef, Suspense } from "react";

function RefreshTokenHandler() {
  const router = useRouter();
  const searchParams = useSearchParams();
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
        router.push(redirectPathname);
      })
      .catch(() => {
        resetSessionCache();
        router.push("/");
      });
  }, [router, redirectPathname]);

  return <Loading />;
}

export default function LogoutPage() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <RefreshTokenHandler />
    </Suspense>
  );
}
