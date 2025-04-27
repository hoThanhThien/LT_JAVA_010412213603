"use client";

import { useEffect, useRef, useState } from "react";
import { Button, Divider, Form, Input, Modal, Space } from "antd";
import type { FormProps } from "antd";
import { EmployeeListType } from "@/schemaValidations/employee.schema";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Upload } from "lucide-react";
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
        username: dataUpdate.username,
        phone: dataUpdate.phone,
        email: dataUpdate.email,
        address: dataUpdate.address,
      });
    }
  }, [dataUpdate, form]);

  const onFinish: FormProps<EmployeeListType>["onFinish"] = async (values) => {
    setIsSubmit(true);
    values.avtUser = previewAvatar;
    console.log(values);

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

          <Form.Item<EmployeeListType>
            labelCol={{ span: 24 }}
            label="Avatar"
            name="avtUser"
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
