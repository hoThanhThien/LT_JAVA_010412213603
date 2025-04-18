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
          service: "",
          status: "",
          store: "",
          image: "",
          price: "",
          description: "",
          createdAt: "2023-10-01",
          updatedAt: "2023-10-01",
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
    {
      title: "Mã đơn",
      dataIndex: "id",
      copyable: true,
    },
    {
      title: "SĐT",
      dataIndex: "phone",
      copyable: true,
      align: "center",
    },
    {
      title: "Dịch vụ",
    },
    {
      title: "Họ tên",
      dataIndex: "username",
      align: "center",
    },
    {
      title: "Trạng thái",
    },
    {
      title: "Cửa hàng",
    },
    {
      title: "Giá tiền",
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
