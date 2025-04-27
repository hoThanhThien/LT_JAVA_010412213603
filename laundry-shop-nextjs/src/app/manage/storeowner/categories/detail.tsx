"use client";

import { useState, useRef } from "react";
import { Button, Divider, Image, Modal } from "antd";
import { SquarePen } from "lucide-react";
import { ProTable } from "@ant-design/pro-components";
import type { ProColumns, ActionType } from "@ant-design/pro-components";

import { ServiceManageType } from "@/schemaValidations/category.schema";
import { EditService } from "./editService";
import { CreateService } from "./createService";

export const Detail = (props: {
  openModalService: boolean;
  categoryId: number;
  setOpenModalService: (v: boolean) => void;
  dataServiceDetail: ServiceManageType[];
  setDataServiceDetail: (v: ServiceManageType[]) => void;
  refreshTable: () => void;
}) => {
  const {
    openModalService,
    setOpenModalService,
    dataServiceDetail,
    setDataServiceDetail,
    refreshTable,
    categoryId,
  } = props;

  const [openModalUpdate, setOpenModalUpdate] = useState<boolean>(false);
  const [dataUpdate, setDataUpdate] = useState<ServiceManageType | null>(null);

  const [openModalCreate, setOpenModalCreate] = useState<boolean>(false);

  // Reference for table actions like refresh
  const actionRef = useRef<ActionType>(null);

  const refreshAndClose = () => {
    setOpenModalService(false), refreshTable();
  };

  // Define ProTable columns
  const columns: ProColumns<ServiceManageType>[] = [
    {
      title: "ID",
      dataIndex: "id",
    },
    {
      title: "Tên",
      dataIndex: "name",
      align: "center",
    },
    {
      title: "Thời gian hoàn thành",
      dataIndex: "estimatedTime",
      align: "center",
    },
    {
      title: "Giá",
      dataIndex: "price",
      align: "center",
    },
    {
      title: "Ảnh",
      align: "center",
      render: (_, entity) => (
        <Image width={100} src={entity.imageDesc} alt="Image" />
      ),
    },
    {
      title: "Mô tả",
      dataIndex: "description",
      width: 300,
    },
    {
      title: "Thao tác",
      valueType: "option",
      align: "center",
      render: (_, entity) => [
        <SquarePen
          key="edit"
          className="cursor-pointer mx-auto"
          onClick={() => {
            setDataUpdate(entity);
            setOpenModalUpdate(true);
          }}
        />,
      ],
    },
  ];

  return (
    <>
      <Modal
        title="Tạo mới"
        open={openModalService}
        width={1000}
        onCancel={() => {
          setOpenModalService(false);
          setDataServiceDetail([]);
        }}
        destroyOnClose={true}
        footer={[
          <Button
            key="add"
            type="primary"
            onClick={() => {
              setOpenModalCreate(true);
            }}
          >
            Thêm mới
          </Button>,
        ]}
      >
        <Divider />
        <ProTable<ServiceManageType>
          columns={columns}
          dataSource={dataServiceDetail}
          actionRef={actionRef}
          rowKey="id"
          search={false}
          toolBarRender={false}
        />
      </Modal>
      <EditService
        openModalUpdate={openModalUpdate}
        setOpenModalUpdate={setOpenModalUpdate}
        dataUpdate={dataUpdate}
        setDataUpdate={setDataUpdate}
        refreshAndClose={refreshAndClose}
      />
      <CreateService
        openModalCreate={openModalCreate}
        setOpenModalCreate={setOpenModalCreate}
        refreshAndClose={refreshAndClose}
        categoryId={categoryId}
      />
    </>
  );
};
