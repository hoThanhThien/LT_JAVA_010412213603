"use client";

import { useRef, useState, useEffect } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { ProTable } from "@ant-design/pro-components";
import { EllipsisIcon, Plus } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { EmployeeListType } from "@/schemaValidations/employee.schema";
import employeeApiRequests from "@/apiRequests/employee";
import { EditEmployeeByStoreOwner } from "./edit";
import { Button } from "antd";
import { CreateEmployeeByStoreOwner } from "./create";

export default function StoreOwnerManageEmployee() {
  const [mounted, setMounted] = useState(false);
  const actionRef = useRef<ActionType>(null);
  const [meta, setMeta] = useState({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0,
  });
  const [currentPage, setCurrentPage] = useState(1); // Bắt đầu từ 1 cho UI
  const [pageSize, setPageSize] = useState(10);
  const [loading, setLoading] = useState(false);

  const [openModalUpdate, setOpenModalUpdate] = useState<boolean>(false);
  const [dataUpdate, setDataUpdate] = useState<EmployeeListType | null>(null);

  const [openModalCreate, setOpenModalCreate] = useState<boolean>(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  const refreshTable = () => {
    actionRef.current?.reload();
  };

  // Hàm fetch data riêng để có thể gọi lại khi cần
  const fetchData = async (page: number, size: number) => {
    setLoading(true);
    try {
      // API thường yêu cầu page bắt đầu từ 0
      const apiPage = page - 1;
      const query = await employeeApiRequests.storeOwner(apiPage, size);
      const res = query.payload;

      if (res.data) {
        setMeta(res.data.meta);
        return {
          data: res.data.result || [],
          success: true,
          total: res.data.meta.totalElements || 0,
        };
      }
      return { data: [], success: false, total: 0 };
    } catch (error) {
      console.error("Failed to fetch orders:", error);
      return { data: [], success: false, total: 0 };
    } finally {
      setLoading(false);
    }
  };

  // Khi component mount, trigger fetch dữ liệu ban đầu
  useEffect(() => {
    if (mounted && actionRef.current) {
      actionRef.current.reload();
    }
  }, [mounted]);

  if (!mounted) {
    return null;
  }

  const columns: ProColumns<EmployeeListType>[] = [
    {
      dataIndex: "index",
      valueType: "indexBorder",
      width: 48,
    },
    {
      title: "Họ tên",
      dataIndex: "username",
    },
    {
      title: "Số điện thoại",
      dataIndex: "phone",
    },
    {
      title: "Email",
      dataIndex: "email",
    },
    {
      title: "Địa chỉ",
      dataIndex: "address",
    },
    // {
    //   title: "Ảnh đại diện",
    //   dataIndex: "avtUser",
    //   align: "center",
    //   render: (_, record) => {
    //     return (
    //       <Avatar className="mx-auto">
    //         <AvatarImage src={record.avtUser} alt="avatar" />
    //         <AvatarFallback>
    //           {record.username.slice(0, 2).toUpperCase() || "NA"}
    //         </AvatarFallback>
    //       </Avatar>
    //     );
    //   },
    // },
    // {
    //   title: "Dịch vụ",
    //   dataIndex: "serviceCategoryName",
    // },
    // {
    //   title: "Cửa hàng",
    //   dataIndex: "laundryShopName",
    // },
    {
      hideInSearch: true,
      align: "center",
      render(_, entity) {
        return (
          <>
            <DropdownMenu modal={false}>
              <DropdownMenuTrigger asChild>
                <EllipsisIcon className="cursor-pointer" />
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuItem
                  onClick={() => {
                    setDataUpdate(entity);
                    setOpenModalUpdate(true);
                  }}
                  className="cursor-pointer"
                >
                  Chỉnh sửa
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
      <ProTable<EmployeeListType>
        columns={columns}
        actionRef={actionRef}
        cardBordered
        search={false}
        request={async () => {
          return await fetchData(currentPage, pageSize);
        }}
        rowKey="username"
        pagination={{
          current: currentPage,
          pageSize: pageSize,
          total: meta.totalElements,
          onChange: (page, size) => {
            setCurrentPage(page);
            setPageSize(size);
          },
          showTotal: (total, range) => (
            <div>
              {range[0]}-{range[1]} trên {total} rows
            </div>
          ),
        }}
        headerTitle="Quản lý nhân viên"
        loading={loading}
        toolBarRender={() => [
          <Button
            key="button"
            icon={<Plus />}
            type="primary"
            onClick={() => {
              setOpenModalCreate(true);
            }}
          >
            Thêm
          </Button>,
        ]}
      />
      <EditEmployeeByStoreOwner
        openModalUpdate={openModalUpdate}
        setOpenModalUpdate={setOpenModalUpdate}
        dataUpdate={dataUpdate}
        setDataUpdate={setDataUpdate}
        refreshTable={refreshTable}
      />
      <CreateEmployeeByStoreOwner
        openModalCreate={openModalCreate}
        setOpenModalCreate={setOpenModalCreate}
        refreshTable={refreshTable}
      />
    </>
  );
}
