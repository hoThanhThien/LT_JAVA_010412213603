import { z } from "zod";

const serverEnvSchema = z.object({
  JWT_SECRET: z.string().min(32, "JWT_SECRET phải dài tối thiểu 32 ký tự"),
});

/**
 * Đọc biến môi trường chỉ dùng ở phía server (Route Handlers, proxy, RSC).
 * Trả về null thay vì crash khi thiếu để middleware có thể từ chối request an toàn.
 */
export function getJwtSecret(): string | null {
  if (typeof window !== "undefined") {
    throw new Error("getJwtSecret() chỉ được gọi ở server");
  }
  const parsed = serverEnvSchema.safeParse({
    JWT_SECRET: process.env.JWT_SECRET,
  });
  if (!parsed.success) {
    console.error("[server-env] JWT_SECRET không hợp lệ:", parsed.error.flatten().fieldErrors);
    return null;
  }
  return parsed.data.JWT_SECRET;
}
