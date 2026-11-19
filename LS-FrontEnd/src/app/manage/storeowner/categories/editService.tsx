"use client";

import { useEffect, useRef, useState } from "react";
import { Button, Divider, Form, Input, Modal, Space } from "antd";
import type { FormProps } from "antd";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Upload } from "lucide-react";
import { toast } from "react-toastify";
import { ServiceManageType } from "@/schemaValidations/category.schema";
import serviceApiRequests from "@/apiRequests/service";
import { compressImageToBase64, handleErrorApi } from "@/lib/utils";

export const EditService = (props: {
  openModalUpdate: boolean;
  setOpenModalUpdate: (v: boolean) => void;
  setDataUpdate: (v: ServiceManageType | null) => void;
  dataUpdate: ServiceManageType | null;
  refreshAndClose: () => void;
}) => {
  const {
    openModalUpdate,
    setOpenModalUpdate,
    setDataUpdate,
    dataUpdate,
    refreshAndClose,
  } = props;
  const [isSubmit, setIsSubmit] = useState<boolean>(false);
  const [isDelete, setIsDelete] = useState<boolean>(false);

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

  useEffect(() => {
    if (dataUpdate) {
      form.setFieldsValue({
        name: dataUpdate.name,
        price: dataUpdate.price,
        description: dataUpdate.description,
        estimatedTime: dataUpdate.estimatedTime,
        id: dataUpdate.id,
      });
    }
  }, [dataUpdate, form]);

  const onFinish: FormProps<ServiceManageType>["onFinish"] = async (values) => {
    const parsedPrice = Number(values.price);
    if (!Number.isFinite(parsedPrice) || parsedPrice <= 0) {
      toast.error("Giá dịch vụ phải là số lớn hơn 0");
      return;
    }
    setIsSubmit(true);
    values.price = parsedPrice;
    values.imageDesc = previewAvatar || dataUpdate?.imageDesc || "";

    try {
      const result = await serviceApiRequests.updateService(values);
      toast.success(result.payload.message, {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: false,
      });
      setIsSubmit(false);
      form.resetFields();
      setPreviewAvatar("");
      setOpenModalUpdate(false);
      setDataUpdate(null);
      refreshAndClose();
    } catch (error) {
      setIsSubmit(false);
      handleErrorApi({ error });
    }
  };

  return (
    <>
      <Modal
        title="Chỉnh sửa dịch vụ"
        open={openModalUpdate}
        onCancel={() => {
          setOpenModalUpdate(false);
          setDataUpdate(null);
          setPreviewAvatar("");
          form.resetFields();
        }}
        confirmLoading={isSubmit}
        destroyOnClose={true}
        afterOpenChange={(open) => {
          if (!open) {
            setPreviewAvatar("");
          }
        }}
        footer={[
          <Button
            key="cancel"
            onClick={() => {
              setOpenModalUpdate(false);
              setDataUpdate(null);
              setPreviewAvatar("");
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
                if (!dataUpdate?.id) return;
                setIsDelete(true);
                const deleteResult = await serviceApiRequests.deleteService(
                  dataUpdate.id
                );
                setOpenModalUpdate(false);
                setDataUpdate(null);
                setPreviewAvatar("");
                toast.success(deleteResult.payload.message, {
                  position: "bottom-left",
                  autoClose: 3000,
                  hideProgressBar: false,
                  closeOnClick: false,
                });
                refreshAndClose();
              } catch (error) {
                handleErrorApi({ error });
              } finally {
                setIsDelete(false);
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
          <Form.Item<ServiceManageType>
            labelCol={{ span: 24 }}
            label="ID"
            name="id"
            hidden
          >
            <Input disabled />
          </Form.Item>

          <Form.Item<ServiceManageType>
            labelCol={{ span: 24 }}
            label="Tên"
            name="name"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item>
            <Space.Compact style={{ display: "flex", gap: "16px" }}>
              <Form.Item<ServiceManageType>
                labelCol={{ span: 24 }}
                label="Giá"
                name="price"
                rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
              >
                <Input />
              </Form.Item>

              <Form.Item<ServiceManageType>
                labelCol={{ span: 24 }}
                label="Thời gian hoàn thành"
                name="estimatedTime"
                rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
              >
                <Input />
              </Form.Item>
            </Space.Compact>
          </Form.Item>

          <Form.Item<ServiceManageType>
            labelCol={{ span: 24 }}
            label="Mô tả"
            name="description"
            rules={[{ required: true, message: "Vui lòng không bỏ trống" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<ServiceManageType>
            labelCol={{ span: 24 }}
            label="Avatar"
            name="imageDesc"
          >
            <div className="flex gap-2 items-start justify-start">
              <Avatar className="aspect-square w-[100px] h-[100px] rounded-md object-cover">
                <AvatarImage src={previewAvatar || dataUpdate?.imageDesc || ""} />
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
