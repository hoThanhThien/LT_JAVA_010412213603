"use client";

import { useEffect, useState } from "react";
import { Button, Divider, Form, Input, Modal, Space } from "antd";
import type { FormProps } from "antd";
import { EmployeeListType } from "@/schemaValidations/employee.schema";
import { useUpdateEmployeeByStoreOwnerMutation } from "@/queries/useEmployee";
import { toast } from "react-toastify";
import employeeApiRequests from "@/apiRequests/employee";

export const EditEmployeeByStoreOwner = (props: {
  openModalUpdate: boolean;
  setOpenModalUpdate: (v: boolean) => void;
  refreshTable: () => void;
  setDataUpdate: (v: EmployeeListType | null) => void;
  dataUpdate: EmployeeListType | null;
}) => {
  const {
    openModalUpdate,
    setOpenModalUpdate,
    refreshTable,
    setDataUpdate,
    dataUpdate,
  } = props;
  const [isSubmit, setIsSubmit] = useState<boolean>(false);
  const [isDelete, setIsDelete] = useState<boolean>(false);

  const updateMutation = useUpdateEmployeeByStoreOwnerMutation();

  // https://ant.design/components/form#components-form-demo-control-hooks
  const [form] = Form.useForm();

  useEffect(() => {
    if (dataUpdate) {
      form.setFieldsValue({
        username: dataUpdate.username,
        phone: dataUpdate.phone,
        email: dataUpdate.email,
        address: dataUpdate.address,
      });
    }
  }, [dataUpdate, form]);

  const onFinish: FormProps<EmployeeListType>["onFinish"] = async (values) => {
    setIsSubmit(true);

    if (updateMutation.isPending) return;
    try {
      const result = await updateMutation.mutateAsync(values);
      toast.success(result.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      setIsSubmit(false);
      form.resetFields();
      setOpenModalUpdate(false);
      setDataUpdate(null);
      refreshTable();
    } catch (error) {
      setIsSubmit(false);
      console.log(error);
    }
  };

  return (
    <>
      <Modal
        title="Chỉnh sửa"
        open={openModalUpdate}
        onCancel={() => {
          setOpenModalUpdate(false);
          setDataUpdate(null);

          form.resetFields();
        }}
        confirmLoading={isSubmit}
        destroyOnClose={true}
        footer={[
          <Button
            key="cancel"
            onClick={() => {
              setOpenModalUpdate(false);
              setDataUpdate(null);
              form.resetFields();
            }}
          >
            Hủy
          </Button>,
          <Button
            key="submit"
            type="primary"
            loading={isSubmit}
            onClick={() => form.submit()}
          >
            Cập nhật
          </Button>,
          <Button
            key="delete"
            loading={isDelete}
            type="primary"
            danger
            onClick={async () => {
              try {
                setIsDelete(true);
                const deleteResult =
                  await employeeApiRequests.deleteByStoreOwner(
                    dataUpdate?.phone!
                  );
                setOpenModalUpdate(false);
                refreshTable();
                toast.success(deleteResult.payload.message, {
                  position: "bottom-left",
                  autoClose: 3000,
                  hideProgressBar: false,
                  closeOnClick: false,
                });
                setIsDelete(false);
              } catch (error) {
                setOpenModalUpdate(false);
                console.log(error);
              }
            }}
          >
            Xoá dữ liệu
          </Button>,
        ]}
      >
        <Divider />

        <Form
          form={form}
          name="form-update"
          style={{ maxWidth: 600 }}
          onFinish={onFinish}
          autoComplete="off"
        >
          <Form.Item>
            <Space.Compact style={{ display: "flex", gap: "16px" }}>
              <Form.Item<EmployeeListType>
                labelCol={{ span: 24 }}
                label="Tên hiển thị"
                name="username"
                rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
              >
                <Input />
              </Form.Item>

              <Form.Item<EmployeeListType>
                labelCol={{ span: 24 }}
                label="Tài khoản"
                name="phone"
                rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
              >
                <Input disabled />
              </Form.Item>
            </Space.Compact>
          </Form.Item>

          <Form.Item>
            <Space.Compact style={{ display: "flex", gap: "16px" }}>
              <Form.Item<EmployeeListType>
                labelCol={{ span: 24 }}
                label="Email"
                name="email"
                rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
              >
                <Input />
              </Form.Item>

              <Form.Item<EmployeeListType>
                labelCol={{ span: 24 }}
                label="Địa chỉ"
                name="address"
                rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
              >
                <Input />
              </Form.Item>
            </Space.Compact>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};
