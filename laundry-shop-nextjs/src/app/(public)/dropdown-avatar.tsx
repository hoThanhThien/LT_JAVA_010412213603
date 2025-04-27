"use client";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { useLogoutMutation } from "@/queries/useAuth";
import { getAccessTokenFromLocalStorage, handleErrorApi } from "@/lib/utils";
import { useRouter } from "next/navigation";
import { useAccountMe } from "@/queries/useAccount";
import { useAuthStore } from "@/lib/zustand";
import jwt from "jsonwebtoken";
import { AvatarImage } from "@radix-ui/react-avatar";

export default function DropdownAvatar() {
  const logoutMutation = useLogoutMutation();
  const router = useRouter();
  const { data } = useAccountMe();
  const account = data?.payload?.data?.account;

  const { setIsAuth, setOpenSetting, setOpenPay } = useAuthStore();

  const decodedAccessToken = jwt.decode(getAccessTokenFromLocalStorage()!) as {
    role: string;
  };

  const logout = async () => {
    if (logoutMutation.isPending) return;
    try {
      await logoutMutation.mutateAsync();
      setIsAuth(false);
      router.push("/");
    } catch (error) {
      handleErrorApi({
        error,
      });
    }
  };

  return (
    <DropdownMenu modal={false}>
      <DropdownMenuTrigger asChild>
        {account && (
          <Button
            variant="outline"
            size="icon"
            className="overflow-hidden rounded-full cursor-pointer"
          >
            <Avatar>
              <AvatarImage src={account?.avtUser ?? undefined} alt="avatar" />
              <AvatarFallback>
                {account?.username?.slice(0, 1).toUpperCase()}
              </AvatarFallback>
            </Avatar>
          </Button>
        )}
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        <DropdownMenuLabel>{account?.username}</DropdownMenuLabel>
        <DropdownMenuSeparator />

        {decodedAccessToken && decodedAccessToken.role === "Customer" && (
          <DropdownMenuItem asChild>
            <div
              className="cursor-pointer"
              onClick={() => {
                setOpenSetting(true);
              }}
            >
              Cài đặt tài khoản
            </div>
          </DropdownMenuItem>
        )}

        {decodedAccessToken && decodedAccessToken.role !== "Admin" && (
          <DropdownMenuItem>
            <div
              className="cursor-pointer"
              onClick={() => {
                setOpenPay(true);
              }}
            >
              Thanh toán
            </div>
          </DropdownMenuItem>
        )}

        <DropdownMenuItem>
          <Link href="https://www.messenger.com/t/331186783932984/">
            Hỗ trợ
          </Link>
        </DropdownMenuItem>
        {decodedAccessToken && decodedAccessToken.role === "Admin" && (
          <DropdownMenuItem>
            <Link href="/manage/admin">Quản lý</Link>
          </DropdownMenuItem>
        )}
        {decodedAccessToken && decodedAccessToken.role === "StoreOwner" && (
          <DropdownMenuItem>
            <Link href="/manage/storeowner">Quản lý</Link>
          </DropdownMenuItem>
        )}
        {decodedAccessToken && decodedAccessToken.role === "Employee" && (
          <DropdownMenuItem>
            <Link href="/manage/employee">Quản lý</Link>
          </DropdownMenuItem>
        )}
        <DropdownMenuSeparator />
        <DropdownMenuItem className="cursor-pointer" onClick={logout}>
          Đăng xuất
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
