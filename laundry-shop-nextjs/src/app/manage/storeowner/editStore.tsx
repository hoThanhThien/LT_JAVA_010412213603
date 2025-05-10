"use client";

import { useEffect, useState } from "react";
import { Button, Divider, Form, Input, Modal } from "antd";
import type { FormProps } from "antd";
import { ShopType } from "@/schemaValidations/store.schema";
import shopApiRequests from "@/apiRequests/shop";
import { toast } from "react-toastify";

export const EditStore = (props: {
  openModalUpdate: boolean;
  setOpenModalUpdate: (v: boolean) => void;
  setDataUpdate: (v: ShopType | null) => void;
  dataUpdate: ShopType | null;
  reloadData?: () => void;
}) => {
  const {
    openModalUpdate,
    setOpenModalUpdate,
    setDataUpdate,
    dataUpdate,
    reloadData,
  } = props;
  const [isSubmit, setIsSubmit] = useState<boolean>(false);

  // https://ant.design/components/form#components-form-demo-control-hooks
  const [form] = Form.useForm();

  useEffect(() => {
    if (dataUpdate) {
      form.setFieldsValue({
        name: dataUpdate.name,
        openingHours: dataUpdate.openingHours,
        address: dataUpdate.address,
        description: dataUpdate.description,
        id: dataUpdate.id,
      });
    }
  }, [dataUpdate, form]);

  const onFinish: FormProps<ShopType>["onFinish"] = async (values) => {
    console.log(values);

    setIsSubmit(true);

    try {
      const result = await shopApiRequests.update(values);
      toast.success(result.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      setIsSubmit(false);
      setOpenModalUpdate(false);
      form.resetFields();
      setDataUpdate(null);
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
