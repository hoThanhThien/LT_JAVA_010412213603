export const Role = {
  Customer: "Customer",
  Employee: "Employee",
  StoreOwner: "StoreOwner",
  Admin: "Admin",
} as const;

export const RoleValues = [
  Role.Customer,
  Role.Employee,
  Role.StoreOwner,
  Role.Admin,
] as const;
