"use client";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { ChevronDown } from "lucide-react";
import Link from "next/link";

export default function DropdownService() {
  return (
    <DropdownMenu modal={false}>
      <DropdownMenuTrigger asChild>
        <div className="hover:text-main cursor-pointer flex items-center justify-center">
          Dịch vụ <ChevronDown />
        </div>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        <DropdownMenuItem>
          <Link href="/dich-vu/giat-hap-giat-kho" className="hover:text-main">
            Giặt hấp, giặt khô
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <Link href="/dich-vu/giat-say" className="hover:text-main">
            Giặt sấy
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <Link href="/dich-vu/ve-sinh-giay" className="hover:text-main">
            Vệ sinh giày
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <Link
            href="/dich-vu/ve-sinh-giat-sofa-nem-rem-tham"
            className="hover:text-main"
          >
            Vệ sinh sofa, nệm, rèm, thảm
          </Link>
        </DropdownMenuItem>
        <DropdownMenuItem>
          <Link href="/dich-vu/ve-sinh-may-lanh" className="hover:text-main">
            Vệ sinh máy lạnh
          </Link>
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
