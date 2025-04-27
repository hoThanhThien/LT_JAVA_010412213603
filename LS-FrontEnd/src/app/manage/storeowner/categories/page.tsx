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
import serviceApiRequests from "@/apiRequests/service";
import {
  CategoryManageType,
  ServiceManageType,
} from "@/schemaValidations/category.schema";
import { Detail } from "./detail";
import { EditCategory } from "./editCategory";
import { Button } from "antd/lib";
import { CreateCategory } from "./createCategory";
import { Image } from "antd";

export default function StoreOwnerManageService() {
  const [mounted, setMounted] = useState(false);
  const actionRef = useRef<ActionType>(null);
  const [meta, setMeta] = useState({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0,
  });

  const [openModalService, setOpenModalService] = useState<boolean>(false);
  const [dataServiceDetail, setDataServiceDetail] = useState<
    ServiceManageType[]
  >([]);

  const [openModalUpdate, setOpenModalUpdate] = useState<boolean>(false);
  const [dataUpdate, setDataUpdate] = useState<CategoryManageType | null>(null);

  const [openModalCreate, setOpenModalCreate] = useState<boolean>(false);

  const [categotyId, setCategoryId] = useState(0);

  const [currentPage, setCurrentPage] = useState(1); // Bắt đầu từ 1 cho UI
  const [pageSize, setPageSize] = useState(10);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  // Hàm fetch data riêng để có thể gọi lại khi cần
  const fetchData = async (page: number, size: number) => {
    setLoading(true);
    try {
      // API thường yêu cầu page bắt đầu từ 0
      const apiPage = page - 1;
      const query = await serviceApiRequests.storeOwner(apiPage, size);
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
      console.error("Failed to fetch:", error);
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

  const refreshTable = () => {
    actionRef.current?.reload();
  };

  const columns: ProColumns<CategoryManageType>[] = [
    {
      dataIndex: "index",
      valueType: "indexBorder",
      width: 48,
    },
    {
      title: "ID",
      dataIndex: "id",
      copyable: true,
      align: "center",
    },
    {
      title: "Tên nhóm dịch vụ",
      dataIndex: "name",
      align: "center",
    },
    {
      title: "Ảnh",
      align: "center",
      render: (_, entity) => (
        <Image width={100} src={entity.imageDesc} alt="image" />
      ),
    },

    {
      title: "Mô tả",
      dataIndex: "description",
      align: "center",
      width: 500,
    },
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
                    setOpenModalService(true);
                    setDataServiceDetail(entity.services);
                    setCategoryId(entity.id!);
                  }}
                  className="cursor-pointer"
                >
                  Thêm dịch vụ
                </DropdownMenuItem>
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
      <ProTable<CategoryManageType>
        columns={columns}
        actionRef={actionRef}
        cardBordered
        search={false}
        request={async () => {
          return await fetchData(currentPage, pageSize);
        }}
        rowKey="id"
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
        headerTitle="Quản lý các dịch vụ"
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
      <Detail
        setOpenModalService={setOpenModalService}
        openModalService={openModalService}
        dataServiceDetail={dataServiceDetail}
        setDataServiceDetail={setDataServiceDetail}
        refreshTable={refreshTable}
        categoryId={categotyId}
      />
      <EditCategory
        openModalUpdate={openModalUpdate}
        setOpenModalUpdate={setOpenModalUpdate}
        dataUpdate={dataUpdate}
        setDataUpdate={setDataUpdate}
        refreshTable={refreshTable}
      />
      <CreateCategory
        openModalCreate={openModalCreate}
        setOpenModalCreate={setOpenModalCreate}
        refreshTable={refreshTable}
      />
    </>
  );
}
