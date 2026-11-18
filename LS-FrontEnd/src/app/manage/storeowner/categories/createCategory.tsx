"use client";

import { useRef, useState } from "react";
import { Button, Divider, Form, Input, Modal } from "antd";
import type { FormProps } from "antd";
import { toast } from "react-toastify";
import { CategoryManageType } from "@/schemaValidations/category.schema";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Upload } from "lucide-react";
import serviceApiRequests from "@/apiRequests/service";
import { compressImageToBase64, handleErrorApi } from "@/lib/utils";

export const CreateCategory = (props: {
  openModalCreate: boolean;
  setOpenModalCreate: (v: boolean) => void;
  refreshTable: () => void;
}) => {
  const { openModalCreate, setOpenModalCreate, refreshTable } = props;

  const [isSubmit, setIsSubmit] = useState<boolean>(false);

  const avatarInputRef = useRef<HTMLInputElement>(null);
  const [previewAvatar, setPreviewAvatar] = useState("");

  // https://ant.design/components/form#components-form-demo-control-hooks
  const [form] = Form.useForm();

  const handleFileChange = async (e: any) => {
    const file = e.target.files?.[0];
    if (file) {
      try {
        const base64 = await compressImageToBase64(file);
        setPreviewAvatar(base64);
      } catch (error: any) {
        toast.error(error?.message || "Không thể xử lý hình ảnh");
      }
    }
  };

  const onFinish: FormProps<CategoryManageType>["onFinish"] = async (
    values
  ) => {
    setIsSubmit(true);

    try {
      const result = await serviceApiRequests.createCategory({
        ...values,
        imageDesc: previewAvatar,
      });
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
      setPreviewAvatar("");
      if (avatarInputRef.current) {
        avatarInputRef.current.value = "";
      }
    } catch (error: any) {
      setIsSubmit(false);
      handleErrorApi({ error });
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
          setPreviewAvatar("");
        }}
        confirmLoading={isSubmit}
        destroyOnClose={true}
        footer={[
          <Button
            key="cancel"
            onClick={() => {
              setOpenModalCreate(false);
              form.resetFields();
              setPreviewAvatar("");
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
          <Form.Item<CategoryManageType>
            labelCol={{ span: 24 }}
            label="Tên"
            name="name"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<CategoryManageType>
            labelCol={{ span: 24 }}
            label="Mô tả"
            name="description"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<CategoryManageType>
            labelCol={{ span: 24 }}
            label="Avatar"
            name="imageDesc"
          >
            <div className="flex gap-2 items-start justify-start">
              <Avatar className="aspect-square w-[100px] h-[100px] rounded-md object-cover">
                <AvatarImage src={previewAvatar} />
                <AvatarFallback className="rounded-none">
                  Hình ảnh
                </AvatarFallback>
              </Avatar>
              <input
                type="file"
                accept="image/*"
                className="absolute w-0 h-0 opacity-0 pointer-events-none"
                style={{ position: "absolute", left: "-9999px" }}
                ref={avatarInputRef}
                onChange={handleFileChange}
              />
              <button
                className="flex aspect-square w-[100px] items-center justify-center rounded-md border border-dashed"
                type="button"
                onClick={() => avatarInputRef.current?.click()}
              >
                <Upload className="h-4 w-4 text-muted-foreground" />
                <span className="sr-only">Upload</span>
              </button>
            </div>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};
