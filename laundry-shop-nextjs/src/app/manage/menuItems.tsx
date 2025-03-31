import type { MenuProps } from "antd";
import { UserRound } from "lucide-react";
import Link from "next/link";

type MenuItem = Required<MenuProps>["items"][number];

export const SiderAdmin: MenuItem[] = [
  {
    key: "account",
    label: <Link href="/manage/admin/account">Tài khoản</Link>,
    icon: <UserRound />,
  },
];
