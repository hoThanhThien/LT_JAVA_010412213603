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
import { handleErrorApi } from "@/lib/utils";
import { useRouter } from "next/navigation";
import { useAccountMe } from "@/queries/useAccount";
import { useAuthStore } from "@/lib/zustand";
import { AvatarImage } from "@radix-ui/react-avatar";

export default function DropdownAvatar() {
  const logoutMutation = useLogoutMutation();
  const router = useRouter();
  const { data } = useAccountMe();
  const account = data?.payload?.data?.account;

  const { setIsAuth, setOpenSetting, setOpenPay, setOpenOrder } = useAuthStore();

  // Role lấy từ thông tin tài khoản (token nằm trong cookie HttpOnly, JS không đọc được)
  const decodedAccessToken = account ? { role: account.role } : null;

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
        <Button
          variant="outline"
          size="icon"
          aria-label="Menu tài khoản"
          className="overflow-hidden rounded-full cursor-pointer border-main/50"
        >
          <Avatar>
            <AvatarImage
              src={account?.avtUser ?? undefined}
              alt={account?.username ?? "avatar"}
            />
            <AvatarFallback>
              {account?.username?.slice(0, 1).toUpperCase() || "U"}
            </AvatarFallback>
          </Avatar>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        <DropdownMenuLabel>
          {account?.username || "Tài khoản"}
        </DropdownMenuLabel>
        <DropdownMenuSeparator />

        {decodedAccessToken && decodedAccessToken.role === "Customer" && (
          <>
            <DropdownMenuItem
              className="cursor-pointer font-medium"
              onClick={() => setOpenOrder(true)}
            >
              Đặt dịch vụ ngay
            </DropdownMenuItem>
            <DropdownMenuItem
              className="cursor-pointer"
              onClick={() => setOpenPay(true)}
            >
              Đơn hàng & Thanh toán
            </DropdownMenuItem>
            <DropdownMenuItem
              className="cursor-pointer"
              onClick={() => setOpenSetting(true)}
            >
              Cài đặt tài khoản
            </DropdownMenuItem>
          </>
        )}

        <DropdownMenuItem asChild>
          <Link href="https://www.messenger.com/t/331186783932984/">
            Hỗ trợ
          </Link>
        </DropdownMenuItem>
        {decodedAccessToken && decodedAccessToken.role === "Admin" && (
          <DropdownMenuItem asChild>
            <Link href="/manage/admin">Quản lý</Link>
          </DropdownMenuItem>
        )}
        {decodedAccessToken && decodedAccessToken.role === "StoreOwner" && (
          <DropdownMenuItem asChild>
            <Link href="/manage/storeowner">Quản lý</Link>
          </DropdownMenuItem>
        )}
        {decodedAccessToken && decodedAccessToken.role === "Employee" && (
          <DropdownMenuItem asChild>
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
