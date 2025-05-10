import type { MenuProps } from "antd";
import {
  Package,
  ScrollText,
  ShieldUser,
  Store,
  UserRound,
  UsersRound,
} from "lucide-react";
import Link from "next/link";

type MenuItem = Required<MenuProps>["items"][number];

export const SiderAdmin: MenuItem[] = [
  {
    key: "storeowner",
    label: <Link href="/manage/admin/storeowner">Cửa hàng trưởng</Link>,
    icon: <ShieldUser />,
  },
  {
    key: "store",
    label: <Link href="/manage/admin/store">Cửa hàng</Link>,
    icon: <Store />,
  },
  {
    key: "employee",
    label: <Link href="/manage/admin/employee">Nhân viên</Link>,
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

export const SiderStoreOwner: MenuItem[] = [
  {
    key: "shop",
    label: <Link href="/manage/storeowner/">Cửa hàng</Link>,
    icon: <Store />,
  },
  {
    key: "employee",
    label: <Link href="/manage/storeowner/employee">Nhân viên</Link>,
    icon: <UsersRound />,
  },
  {
    key: "package",
    label: <Link href="/manage/storeowner/order">Đơn hàng</Link>,
    icon: <Package />,
  },
  {
    key: "categories",
    label: <Link href="/manage/storeowner/categories">Dịch vụ</Link>,
    icon: <ScrollText />,
  },
];

export const SiderEmployee: MenuItem[] = [
  {
    key: "package",
    label: <Link href="/manage/employee/order">Đơn hàng</Link>,
    icon: <Package />,
  },
];
