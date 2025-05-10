"use client";

import Loading from "@/components/loading";
import {
  getAccessTokenFromLocalStorage,
  getRefreshTokenFromLocalStorage,
} from "@/lib/utils";
import { useAuthStore } from "@/lib/zustand";
import { useLogoutMutation } from "@/queries/useAuth";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect, useRef, Suspense } from "react";

function LogoutHandler() {
  const { mutateAsync } = useLogoutMutation();
  const { setIsAuth, setOpenAuth } = useAuthStore();
  const searchParams = useSearchParams();
  const refreshTokenFromUrl = searchParams.get("refreshToken");
  const accessTokenFromUrl = searchParams.get("accessToken");

  const router = useRouter();
  const ref = useRef<any>(null);

  useEffect(() => {
    if (
      ref.current ||
      (refreshTokenFromUrl &&
        refreshTokenFromUrl !== getRefreshTokenFromLocalStorage()) ||
      (accessTokenFromUrl &&
        accessTokenFromUrl !== getAccessTokenFromLocalStorage())
    ) {
      ref.current = mutateAsync;

      mutateAsync().then((res) => {
        setTimeout(() => {
          ref.current = null;
        }, 1000);
        setIsAuth(false);
        router.push("/");
        // setTimeout(() => {
        //   setOpenAuth(false);
        // }, 1000);
      });
    } else {
      router.push("/");
    }
  }, [mutateAsync, router, accessTokenFromUrl, refreshTokenFromUrl, setIsAuth]);
  return <Loading />;
}

export default function Logout() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <LogoutHandler />
    </Suspense>
  );
}
