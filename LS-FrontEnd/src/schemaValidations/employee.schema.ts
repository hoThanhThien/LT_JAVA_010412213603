import { RoleValues } from "@/constants/type";
import z from "zod";
import { MetaSchema } from "./pagination.schema";

const EmployeeListSchema = z.object({
  username: z.string(),
  password: z.string().nullable(),
  email: z.string().email().optional(),
  phone: z.string(),
  address: z.string().optional(),
  role: z.enum(RoleValues),
  avtUser: z.string().optional(),
  createdAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
  usernameStoreOwner: z.string().nullable(),
  shopName: z.string(),
});

export type EmployeeListType = z.TypeOf<typeof EmployeeListSchema>;

export const EmployeeListResSchema = z
  .object({
    data: z.object({
      meta: MetaSchema,
      result: z.array(EmployeeListSchema),
    }),
    message: z.string(),
  })
  .strict();

export type EmployeeListResType = z.TypeOf<typeof EmployeeListResSchema>;

export const EmployeeBody = z
  .object({
    phone: z.string(),
    username: z.string(),
    password: z.string(),
    email: z.string().email().optional(),
    avtUser: z.string().optional(),
    address: z.string().optional(),
  })
  .strict();

export type EmployeeType = z.TypeOf<typeof EmployeeBody>;

export const UpdateEmployeeBody = z
  .object({
    phone: z.string().optional(),
    username: z.string().optional(),
    email: z.string().email().optional(),
    avtUser: z.string().optional(),
    address: z.string().optional(),
  })
  .strict();

export type UpdateEmployeeType = z.TypeOf<typeof UpdateEmployeeBody>;

export const UpdateEmployeeResSchema = z
  .object({
    data: z.object({
      username: z.string(),
      password: z.string().optional().nullable(),
      email: z.string().email().nullable(),
      phone: z.string(),
      address: z.string().nullable(),
      role: z.string(),
      avtUser: z.string().nullable().optional(),
      createdAt: z.string().datetime(),
      updatedAt: z.string().datetime(),
      usernameStoreOwner: z.string().optional().nullable(),
    }),
    message: z.string(),
  })
  .strict();

export type UpdateEmployeeResType = z.TypeOf<typeof UpdateEmployeeResSchema>;

export const DeleteEmployeeBody = z.string();

export type DeleteEmployeeType = z.TypeOf<typeof DeleteEmployeeBody>;

export const DeleteEmployeeResSchema = z
  .object({
    data: z.any().nullable().optional(),
    message: z.string(),
  })
  .strict();

export type DeleteEmployeeResType = z.TypeOf<typeof DeleteEmployeeResSchema>;

const AdminEmployeeBody = z.object({
  username: z.string(),
  email: z.string().email().optional(),
  phone: z.string(),
  address: z.string().optional(),
  role: z.enum(RoleValues),
  createdAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
});

export type AdminEmployeeType = z.TypeOf<typeof AdminEmployeeBody>;

export const AdminEmployeeResBody = z
  .object({
    data: z.object({
      meta: MetaSchema,
      result: z.array(z.object({})),
    }),
    message: z.string(),
  })
  .strict();

export type AdminEmployeeResType = z.TypeOf<typeof AdminEmployeeResBody>;
