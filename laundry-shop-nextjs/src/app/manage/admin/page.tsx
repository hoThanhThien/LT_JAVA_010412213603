"use client";

import { useRef, useState, useEffect } from "react";
import type { ActionType, ProColumns } from "@ant-design/pro-components";
import { AccountType } from "@/schemaValidations/account.schema";
import { ProTable } from "@ant-design/pro-components";

export default function Admin() {
  const [mounted, setMounted] = useState(false);
  const actionRef = useRef<ActionType>(null);

  useEffect(() => {
    setMounted(true);
  }, []);

  if (!mounted) {
    return null;
  }

  const columns: ProColumns<AccountType>[] = [
    {
      title: "ID",
      dataIndex: "id",
      copyable: true,
    },
    {
      title: "Họ tên",
      dataIndex: "name",
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
    {
      title: "Phân quyền",
      dataIndex: "role",
      copyable: true,
      align: "center",
    },
    {
      title: "Trạng thái",
      //   dataIndex: "status",
      hideInSearch: true,
      align: "center",
      //   render: (_, record) => (
      //     <Tag
      //       color={
      //         record.status === "Chờ xác nhận"
      //           ? "gold"
      //           : record.status === "Đã xác nhận"
      //           ? "green"
      //           : "red"
      //       }
      //     >
      //       {record.status}
      //     </Tag>
      //   ),
    },
    {
      title: "Tạo",
      //   dataIndex: "createdAt",
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
      //   dataIndex: "updatedAt",
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
      title: "Action",
      hideInSearch: true,
      align: "center",
      // render(dom, entity, index, action, schema) {
      //   return (
      //     <>
      //       <EyeOutlined
      //         style={{ cursor: "pointer" }}
      //         onClick={() => {
      //           setOpenViewDetail(true);
      //           setDataViewDetail(entity);
      //         }}
      //       />
      //       <EditTwoTone
      //         twoToneColor="#f57800"
      //         style={{ cursor: "pointer", margin: "0 20px" }}
      //         onClick={() => {
      //           setDataUpdate(entity);
      //           setOpenModalUpdate(true);
      //         }}
      //       />
      //       <Popconfirm
      //         placement="leftTop"
      //         title={"Xác nhận đã hoàn thành lịch khám"}
      //         description={"Bạn có chắc chắn hoàn thành lịch khám này ?"}
      //         onConfirm={() => {
      //           setDataDone(entity);
      //           setOpenModalDone(true);
      //         }}
      //         okText="Xác nhận"
      //         cancelText="Hủy"
      //       >
      //         <span style={{ cursor: "pointer", marginRight: "20px" }}>
      //           <CheckOutlined
      //             twoToneColor="#ff4d4f"
      //             style={{ cursor: "pointer" }}
      //           />
      //         </span>
      //       </Popconfirm>
      //       <Popconfirm
      //         placement="leftTop"
      //         title={"Xác nhận xóa lịch khám"}
      //         description={"Bạn có chắc chắn muốn xóa lịch khám này ?"}
      //         onConfirm={() => handleDeleteBooking(entity.account)}
      //         okText="Xác nhận"
      //         cancelText="Hủy"
      //         okButtonProps={{ loading: isDeleteBooking }}
      //       >
      //         <span style={{ cursor: "pointer" }}>
      //           <DeleteTwoTone
      //             twoToneColor="#ff4d4f"
      //             style={{ cursor: "pointer" }}
      //           />
      //         </span>
      //       </Popconfirm>
      //     </>
      //   );
      // },
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
          return {
            page: 1,
            success: true,
          };
        }}
        rowKey="id"
        pagination={{
          showSizeChanger: true,
          showTotal: (total, range) => (
            <div>
              {range[0]}-{range[1]} trên {total} rows
            </div>
          ),
        }}
        headerTitle="Quản lý tài khoản"
      />
    </>
  );
}
