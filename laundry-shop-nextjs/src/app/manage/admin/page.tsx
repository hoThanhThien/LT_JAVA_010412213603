import Link from "next/link";
import React from "react";

export default function AdminPage() {
  return (
    <div>
      AdminPage
      <Link href="/manage/admin/account">Tài khoản</Link>
    </div>
  );
}
