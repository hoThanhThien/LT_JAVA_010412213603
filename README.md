# LT_JAVA_010412213603 — Phần mềm quản lý giặt ủi online (Chủ đề số 10)

- **Học phần:** Lập trình Java
- **Nhóm thực hiện:** Thành Thiện, Minh Quang, Minh Chí, Triều Dương, Cao Phi

Ứng dụng web quản lý đặt lịch và vận hành dịch vụ giặt ủi theo mô hình nhiều cửa hàng với 4 vai trò: **Admin**, **StoreOwner** (chủ cửa hàng), **Employee** (nhân viên), và **Customer** (khách hàng).

---

## 1. Kiến trúc & Công nghệ

| Thành phần | Thư mục | Công nghệ chính |
|---|---|---|
| **Backend API** | [`LS-BackEnd/laundry`](LS-BackEnd/laundry) | Java 25, Spring Boot 4.1.2, Spring Security, Spring Data JPA, MySQL / MariaDB, H2 (test), JJWT 0.12.6, Firebase Admin SDK 9.4.3, Spring Mail |
| **Frontend Web** | [`LS-FrontEnd`](LS-FrontEnd) | Next.js 16.4.6 (App Router), React 19, TypeScript 5, Tailwind CSS v4, Ant Design 5, TanStack Query v5, Zod, Firebase Web SDK 11.6 |

### Điểm chính về xác thực & bảo mật
- **Cookie HttpOnly + BFF Proxy:** Frontend lưu `accessToken` và `refreshToken` trong cookie `HttpOnly` qua Next.js Route Handlers (`/api/auth/*`) thay vì lưu ở `localStorage`. Mọi request cần xác thực đi qua `/api/backend/[...path]` để server Next.js tự gắn header `Authorization: Bearer`.
- **Xác thực JWT & Phân quyền Route:** [`src/proxy.ts`](LS-FrontEnd/src/proxy.ts) xác thực chữ ký `HS256` (`issuer: LAUNDRY`, dùng `JWT_SECRET` phía server) và kiểm tra đúng vai trò trước khi cho truy cập `/manage/admin`, `/manage/storeowner`, `/manage/employee`.
- **Thu hồi token (Blacklist) lưu DB:** Token đã logout được lưu vào bảng blacklist trong database thay vì giữ trên RAM.
- **CORS tập trung theo biến môi trường:** Backend đọc `CORS_ALLOWED_ORIGINS` từ `.env` trong `SecurityConfig` thay vì hardcode ở từng controller.

Xem tài liệu chi tiết từng phần:
- [README Backend (`LS-BackEnd`)](LS-BackEnd/README.md)
- [README Frontend (`LS-FrontEnd`)](LS-FrontEnd/README.md)

---

## 2. Yêu cầu môi trường

- **Java JDK 25** (Maven Wrapper `mvnw` / `mvnw.cmd` đã kèm sẵn trong `LS-BackEnd/laundry`)
- **Node.js >= 20** và **npm**
- **MySQL** hoặc **MariaDB** (ví dụ XAMPP, mặc định cổng `3306`)

---

## 3. Hướng dẫn chạy Local

### Bước 1: Tạo Database
Khởi động MySQL/MariaDB và tạo database `laundry`:

```sql
CREATE DATABASE IF NOT EXISTS laundry CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Bước 2: Cấu hình & Chạy Backend (`http://localhost:8080`)
1. Tạo file `LS-BackEnd/laundry/.env` từ mẫu [`LS-BackEnd/laundry/.env.example`](LS-BackEnd/laundry/.env.example).
2. Cập nhật tối thiểu các biến sau trong `.env`:
   - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` khớp với MySQL local.
   - `JWT_SECRET`: chuỗi bí mật ngẫu nhiên dài **tối thiểu 32 ký tự**.
   - `CORS_ALLOWED_ORIGINS=http://localhost:3000,http://127.0.0.1:3000,https://oauth.casso.vn`
   - `APP_SEED_ENABLED=true` (nếu muốn tự tạo dữ liệu mẫu khi database chưa có người dùng nào).
3. Chạy backend:

```powershell
cd LS-BackEnd\laundry
.\mvnw.cmd spring-boot:run
```

> Lưu ý: Không commit file `.env` hay `firebase-service-account.json` lên Git (đã được cấu hình bỏ qua trong `.gitignore`).

### Bước 3: Cấu hình & Chạy Frontend (`http://localhost:3000`)
1. Tạo file `LS-FrontEnd/.env.local` từ mẫu [`LS-FrontEnd/.env.example`](LS-FrontEnd/.env.example).
2. Điền cấu hình cơ bản:

```env
NEXT_PUBLIC_API_ENDPOINT=http://localhost:8080
NEXT_PUBLIC_URL=http://localhost:3000
JWT_SECRET=<giống_hệt_JWT_SECRET_của_Backend_tối_thiểu_32_ký_tự>
```

3. Cài đặt phụ thuộc và chạy dev server:

```powershell
cd LS-FrontEnd
npm install
npm run dev
```

Mở trình duyệt tại `http://localhost:3000`.

---

## 4. Dữ liệu & Tài khoản mẫu Local

Khi bật `APP_SEED_ENABLED=true` và database chưa có bản ghi người dùng nào, [`DataSeeder`](LS-BackEnd/laundry/src/main/java/com/example/laundry/config/DataSeeder.java) tự động khởi tạo 1 cửa hàng mẫu (**HERAMO Quận 1**), 2 danh mục dịch vụ, 2 dịch vụ và 4 tài khoản (mật khẩu chung `123456`):

| Vai trò | Số điện thoại đăng nhập | Email | Trang quản trị |
|---|---|---|---|
| **Admin** | `0900000001` | `admin@laundry.local` | `/manage/admin` |
| **StoreOwner** | `0900000002` | `storeowner@laundry.local` | `/manage/storeowner` |
| **Employee** | `0900000003` | `employee@laundry.local` | `/manage/employee` |
| **Customer** | `0900000004` | `customer@laundry.local` | Đặt đơn trên `/` |

---

## 5. Dịch vụ tích hợp tùy chọn

- **Firebase Phone OTP:** Cần đặt file `firebase-service-account.json` vào `LS-BackEnd/laundry/src/main/resources/` (hoặc trỏ qua `FIREBASE_CONFIG_PATH`) và điền các biến `NEXT_PUBLIC_FIREBASE_*` ở `LS-FrontEnd/.env.local`. Nếu thiếu cấu hình Firebase, backend sẽ tự bỏ qua khởi tạo `FirebaseAuth`; các luồng đăng nhập, đặt đơn, quản trị vẫn hoạt động bình thường (chỉ luồng đăng ký OTP SĐT không khả dụng).
- **VietQR & Casso:** Cấu hình `BANK_ACCOUNT_HOLDER`, `BANK_ACCOUNT_NUMBER`, `VIET_QR_URL` để tạo mã QR thanh toán và `CASSO_API_KEY` để polling đối soát giao dịch.
- **Email SMTP:** Cấu hình `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD` khi cần gửi thông báo email.

---

## 6. Kiểm tra & Build

- **Backend test (chạy offline với H2 in-memory, không cần MySQL):**

```powershell
cd LS-BackEnd\laundry
.\mvnw.cmd -B -o test
```

- **Frontend type-check, lint & build:**

```powershell
cd LS-FrontEnd
npx tsc --noEmit
npm run lint
npm run build
```
