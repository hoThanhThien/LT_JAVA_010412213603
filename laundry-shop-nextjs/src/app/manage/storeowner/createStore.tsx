"use client";

import { useState } from "react";
import { Button, Divider, Form, Input, Modal } from "antd";
import type { FormProps } from "antd";
import { ShopType } from "@/schemaValidations/store.schema";
import shopApiRequests from "@/apiRequests/shop";
import { toast } from "react-toastify";

export const CreateStore = (props: {
  openModalCreate: boolean;
  setOpenModalCreate: (v: boolean) => void;
  reloadData?: () => void;
}) => {
  const { openModalCreate, setOpenModalCreate, reloadData } = props;
  const [isSubmit, setIsSubmit] = useState<boolean>(false);

  // https://ant.design/components/form#components-form-demo-control-hooks
  const [form] = Form.useForm();

  const onFinish: FormProps<ShopType>["onFinish"] = async (values) => {
    console.log(values);

    setIsSubmit(true);

    try {
      const result = await shopApiRequests.create(values);
      toast.success(result.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      setIsSubmit(false);
      setOpenModalCreate(false);
      form.resetFields();
      if (reloadData) {
        reloadData();
      }
    } catch (error) {
      setIsSubmit(false);
      console.log(error);
    }
  };

  return (
    <>
      <Modal
        title="Tạo cửa hàng mới"
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
          <Form.Item<ShopType>
            labelCol={{ span: 24 }}
            label="ID"
            name="id"
            hidden
          >
            <Input disabled />
          </Form.Item>

          <Form.Item<ShopType>
            labelCol={{ span: 24 }}
            label="Tên"
            name="name"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<ShopType>
            labelCol={{ span: 24 }}
            label="Giờ mở cửa"
            name="openingHours"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<ShopType>
            labelCol={{ span: 24 }}
            label="Địa chỉ"
            name="address"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<ShopType>
            labelCol={{ span: 24 }}
            label="Mô tả"
            name="description"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};
