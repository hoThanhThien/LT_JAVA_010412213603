export const Role = {
  Customer: "Customer",
  Employee: "Employee",
  StoreManager: "StoreManager",
  Admin: "Admin",
} as const;

export const RoleValues = [
  Role.Customer,
  Role.Employee,
  Role.StoreManager,
  Role.Admin,
] as const;
