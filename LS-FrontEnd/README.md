# LS-FrontEnd — Giao diện Web Dịch Vụ Giặt Ủi

Ứng dụng Frontend xây dựng bằng **Next.js 16.4.6** (App Router), **React 19**, **TypeScript 5**, **Tailwind CSS v4**, **Ant Design 5**, **TanStack Query v5** và **Zod**.

---

## 1. Cấu hình biến môi trường (`.env.local`)

Tạo file `LS-FrontEnd/.env.local` từ mẫu [`.env.example`](.env.example):

```env
# Public (hiển thị phía trình duyệt)
NEXT_PUBLIC_API_ENDPOINT=http://localhost:8080
NEXT_PUBLIC_URL=http://localhost:3000

# Firebase Web Client (tùy chọn — dùng cho luồng OTP đăng ký SĐT)
NEXT_PUBLIC_FIREBASE_API_KEY=...
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=...
NEXT_PUBLIC_FIREBASE_PROJECT_ID=...
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=...
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=...
NEXT_PUBLIC_FIREBASE_APP_ID=...
NEXT_PUBLIC_FIREBASE_MEASUREMENT_ID=...

# Chỉ dùng phía Server Next.js (TUYỆT ĐỐI KHÔNG thêm tiền tố NEXT_PUBLIC_)
# Phải dài >= 32 ký tự và khớp với JWT_SECRET ở Backend
JWT_SECRET=change-me-use-a-long-random-secret
```

- Các biến `NEXT_PUBLIC_*` được kiểm tra hợp lệ tại [`src/config.ts`](src/config.ts).
- `JWT_SECRET` được đọc chỉ ở phía server qua [`src/lib/server-env.ts`](src/lib/server-env.ts).

---

## 2. Kiến trúc Auth & Bảo vệ Route

- **HttpOnly Cookie & BFF Proxy:**
  - Route Handlers `/api/auth/login`, `/api/auth/logout`, `/api/auth/refresh-token`, `/api/auth/register`, `/api/auth/session` lưu và quản lý `accessToken` / `refreshToken` dưới dạng cookie `HttpOnly`, không lộ JWT cho JavaScript phía client.
  - [`src/app/api/backend/[...path]/route.ts`](src/app/api/backend/%5B...path%5D/route.ts) làm proxy trung gian: đọc cookie `HttpOnly`, tự refresh access token nếu hết hạn, và gắn `Authorization: Bearer` khi gọi tới Backend Spring Boot.
- **Bảo vệ trang quản trị ([`src/proxy.ts`](src/proxy.ts)):**
  - Xác thực chữ ký JWT (`HS256`, `issuer: LAUNDRY`) và đối chiếu claim `role` cho các nhóm route:
    - `/manage/admin/**` → chỉ `Admin`
    - `/manage/storeowner/**` → chỉ `StoreOwner`
    - `/manage/employee/**` → chỉ `Employee`

---

## 3. Các nhóm trang chính

- **Trang công khai & đặt dịch vụ:** `/`, `/dich-vu/*`, `/doi-tac`, `/nhuong-quyen`, `/tuyen-dung`, `/ve-chung-toi`, `/thanh-toan`
- **Quản trị hệ thống:** `/manage/admin` (`storeowner`, `store`, `employee`, `customer`, `order`, `order_done`)
- **Chủ cửa hàng:** `/manage/storeowner` (`categories`, `employee`, `order`)
- **Nhân viên:** `/manage/employee`

---

## 4. Lệnh phát triển & Kiểm tra

```powershell
cd LS-FrontEnd

# Cài đặt packages
npm install

# Chạy dev server (Turbopack) tại http://localhost:3000
npm run dev

# Kiểm tra TypeScript, ESLint và build production
npx tsc --noEmit
npm run lint
npm run build
```
