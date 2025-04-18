import type { MenuProps } from "antd";
import { Package, Store, UserRound, UsersRound } from "lucide-react";
import Link from "next/link";

type MenuItem = Required<MenuProps>["items"][number];

export const SiderAdmin: MenuItem[] = [
  {
    key: "storeOwner",
    label: <Link href="/manage/admin/account">Cửa hàng trưởng</Link>,
    icon: <Store />,
  },
  {
    key: "employee",
    label: <Link href="/manage/admin/account">Nhân viên</Link>,
    icon: <UsersRound />,
  },
  {
    key: "customer",
    label: <Link href="/manage/admin/customer">Khách hàng</Link>,
    icon: <UserRound />,
  },
  {
    key: "package",
    label: <Link href="/manage/admin/order">Đơn hàng</Link>,
    icon: <Package />,
  },
];
