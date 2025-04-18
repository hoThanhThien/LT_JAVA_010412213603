"use client";

import { useRef, useState, useEffect } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { AccountType } from "@/schemaValidations/account.schema";
import { ProTable } from "@ant-design/pro-components";
import { Ellipsis, EllipsisIcon } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

export default function AdminAccount() {
  const [mounted, setMounted] = useState(false);
  const actionRef = useRef<ActionType>(null);
  const [meta, setMeta] = useState({
    current: 1,
    pageSize: 10,
    total: 0,
  });

  const res = {
    data: {
      meta: {
        current: 1,
        pageSize: 10,
        total: 2,
      },
      result: [
        {
          id: "abc",
          username: "Cao Phi",
          phone: "0987654321",
          role: "Customer",
          email: "caophi@@",
          address: "Hà Nội",
          createdAt: "2023-10-01",
          updatedAt: "2023-10-01",
        },
        {
          id: "def",
          username: "Nguyen Van A",
          phone: "0987123456",
          role: "Customer",
          email: "nguyenvana@example.com",
          address: "Hồ Chí Minh",
          createdAt: "2023-10-05",
          updatedAt: "2023-10-05",
        },
        {
          id: "ghi",
          username: "Tran Thi B",
          phone: "0912345678",
          role: "Admin",
          email: "tranthi@example.com",
          address: "Đà Nẵng",
          createdAt: "2023-10-10",
          updatedAt: "2023-10-15",
        },
        {
          id: "jkl",
          username: "Le Van C",
          phone: "0923456789",
          role: "Customer",
          email: "levanc@example.com",
          address: "Hải Phòng",
          createdAt: "2023-10-12",
          updatedAt: "2023-10-12",
        },
        {
          id: "mno",
          username: "Pham Thi D",
          phone: "0934567890",
          role: "Customer",
          email: "phamthid@example.com",
          address: "Cần Thơ",
          createdAt: "2023-10-15",
          updatedAt: "2023-10-20",
        },
        {
          id: "pqr",
          username: "Hoang Van E",
          phone: "0945678901",
          role: "Manager",
          email: "hoangvane@example.com",
          address: "Nha Trang",
          createdAt: "2023-10-18",
          updatedAt: "2023-10-18",
        },
        {
          id: "stu",
          username: "Nguyen Thi F",
          phone: "0956789012",
          role: "Customer",
          email: "nguyenthif@example.com",
          address: "Đà Lạt",
          createdAt: "2023-10-20",
          updatedAt: "2023-10-25",
        },
        {
          id: "vwx",
          username: "Tran Van G",
          phone: "0967890123",
          role: "Customer",
          email: "tranvang@example.com",
          address: "Huế",
          createdAt: "2023-10-22",
          updatedAt: "2023-10-22",
        },
        {
          id: "yz1",
          username: "Le Thi H",
          phone: "0978901234",
          role: "Support",
          email: "lethih@example.com",
          address: "Vũng Tàu",
          createdAt: "2023-10-25",
          updatedAt: "2023-10-28",
        },
        {
          id: "234",
          username: "Pham Van I",
          phone: "0989012345",
          role: "Customer",
          email: "phamvani@example.com",
          address: "Quy Nhơn",
          createdAt: "2023-10-30",
          updatedAt: "2023-10-30",
        },
      ],
    },
    message: "Lấy thông tin thành công",
  };

  useEffect(() => {
    setMounted(true);
  }, []);

  if (!mounted) {
    return null;
  }

  const columns: ProColumns<AccountType>[] = [
    // {
    //   title: "ID",
    //   dataIndex: "id",
    //   copyable: true,
    // },
    {
      title: "Họ tên",
      dataIndex: "username",
      align: "center",
    },
    {
      title: "SĐT",
      dataIndex: "phone",
      copyable: true,
      align: "center",
    },
    {
      title: "Email",
      dataIndex: "email",
      copyable: true,
      align: "center",
    },
    // {
    //   title: "Trạng thái",
    //     dataIndex: "status",
    //   hideInSearch: true,
    //   align: "center",
    //     render: (_, record) => (
    //       <Tag
    //         color={
    //           record.status === "Chờ xác nhận"
    //             ? "gold"
    //             : record.status === "Đã xác nhận"
    //             ? "green"
    //             : "red"
    //         }
    //       >
    //         {record.status}
    //       </Tag>
    //     ),
    // },
    {
      title: "Tạo",
      dataIndex: "createdAt",
      hideInSearch: true,
      align: "center",
      //   render: (createdAt) => {
      //     if (typeof createdAt === "string" || typeof createdAt === "number") {
      //       return dayjs(createdAt).format("DD-MM-YYYY");
      //     }
      //     return "--";
      //   },
    },
    {
      title: "Cập nhập",
      dataIndex: "updatedAt",
      hideInSearch: true,
      align: "center",
      //   render: (updatedAt) => {
      //     if (typeof updatedAt === "string" || typeof updatedAt === "number") {
      //       return dayjs(updatedAt).format("DD-MM-YYYY");
      //     }
      //     return "--";
      //   },
    },
    {
      title: " ",
      hideInSearch: true,
      align: "center",
      render() {
        return (
          <>
            <DropdownMenu modal={false}>
              <DropdownMenuTrigger asChild>
                <EllipsisIcon className="cursor-pointer" />
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuItem className="cursor-pointer">
                  Xem thông tin
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </>
        );
      },
    },
  ];

  return (
    <>
      <ProTable<AccountType>
        columns={columns}
        actionRef={actionRef}
        cardBordered
        search={{
          searchText: "Tìm kiếm",
          resetText: "Làm mới",
          defaultColsNumber: 6,

          split: true,
          collapseRender: (collapsed) =>
            collapsed ? "Mở rộng +" : "Thu gọn -",
        }}
        form={{
          syncToUrl: false,
          ignoreRules: false,
          submitter: {
            searchConfig: {
              submitText: "Tìm kiếm",
              resetText: "Làm mới",
            },
          },
        }}
        request={async (params, sort, filter) => {
          if (res.data) {
            setMeta(res.data.meta);
          }
          return {
            data: res.data.result,
            page: 1,
            success: true,
            total: res.data.meta.total,
          };
        }}
        rowKey="id"
        pagination={{
          current: meta.current,
          pageSize: meta.pageSize,
          showSizeChanger: true,
          showTotal: (total, range) => (
            <div>
              {range[0]}-{range[1]} trên {total} rows
            </div>
          ),
        }}
        headerTitle="Quản lý khách hàng"
      />
    </>
  );
}
