"use client";

import { useEffect, useRef, useState } from "react";
import { Button, Divider, Form, Input, Modal, Space } from "antd";
import type { FormProps } from "antd";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Upload } from "lucide-react";
import { toast } from "react-toastify";
import { ServiceManageType } from "@/schemaValidations/category.schema";
import serviceApiRequests from "@/apiRequests/service";

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

  const convertToBase64 = (file: File) => {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);

      fileReader.onload = () => {
        resolve(fileReader.result);
      };

      fileReader.onerror = (error) => {
        reject(error);
      };
    });
  };

  const handleFileChange = async (e: any) => {
    const file = e.target.files?.[0];
    if (file) {
      try {
        const base64: any = await convertToBase64(file);
        setPreviewAvatar(base64); // Lưu base64 vào state để hiển thị preview
      } catch (error) {
        console.error("Error converting image to base64:", error);
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
      setPreviewAvatar(dataUpdate.imageDesc);
    }
  }, [dataUpdate, form]);

  const onFinish: FormProps<ServiceManageType>["onFinish"] = async (values) => {
    setIsSubmit(true);
    values.imageDesc = previewAvatar;

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
      setOpenModalUpdate(false);
      setDataUpdate(null);
      refreshAndClose();
    } catch (error) {
      setIsSubmit(false);
      setOpenModalUpdate(false);
      console.log(error);
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
                setIsDelete(true);
                const deleteResult = await serviceApiRequests.deleteService(
                  dataUpdate?.id!
                );
                setOpenModalUpdate(false);
                toast.success(deleteResult.payload.message, {
                  position: "bottom-left",
                  autoClose: 3000,
                  hideProgressBar: false,
                  closeOnClick: false,
                });
                refreshAndClose();
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
