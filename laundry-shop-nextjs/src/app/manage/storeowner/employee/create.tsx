"use client";

import { useState } from "react";
import { Button, Divider, Form, Input, Modal, Space } from "antd";
import type { FormProps } from "antd";
import { EmployeeType } from "@/schemaValidations/employee.schema";
import { toast } from "react-toastify";
import employeeApiRequests from "@/apiRequests/employee";

export const CreateEmployeeByStoreOwner = (props: {
  openModalCreate: boolean;
  setOpenModalCreate: (v: boolean) => void;
  refreshTable: () => void;
}) => {
  const { openModalCreate, setOpenModalCreate, refreshTable } = props;
  const [isSubmit, setIsSubmit] = useState<boolean>(false);

  // https://ant.design/components/form#components-form-demo-control-hooks
  const [form] = Form.useForm();

  const onFinish: FormProps<EmployeeType>["onFinish"] = async (values) => {
    setIsSubmit(true);

    try {
      const result = await employeeApiRequests.createByStoreOwner(values);
      toast.success(result.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      setIsSubmit(false);
      form.resetFields();
      setOpenModalCreate(false);
      refreshTable();
    } catch (error: any) {
      setIsSubmit(false);
      toast.error(error, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
    }
  };

  return (
    <>
      <Modal
        title="Tạo mới"
        open={openModalCreate}
        onCancel={() => {
          setOpenModalCreate(false);
          form.resetFields();
        }}
        confirmLoading={isSubmit}
        destroyOnClose={true}
        footer={[
          <Button
            key="cancel"
            onClick={() => {
              setOpenModalCreate(false);
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
            Tạo
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
          <Form.Item<EmployeeType>
            labelCol={{ span: 24 }}
            label="Tên hiển thị"
            name="username"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<EmployeeType>
            labelCol={{ span: 24 }}
            label="Tài khoản"
            name="phone"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<EmployeeType>
            labelCol={{ span: 24 }}
            label="Mật khẩu"
            name="password"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item>
            <Space.Compact style={{ display: "flex", gap: "16px" }}>
              <Form.Item<EmployeeType>
                labelCol={{ span: 24 }}
                label="Email"
                name="email"
              >
                <Input />
              </Form.Item>

              <Form.Item<EmployeeType>
                labelCol={{ span: 24 }}
                label="Địa chỉ"
                name="address"
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
