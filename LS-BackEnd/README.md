# LS-BackEnd — Backend Dịch Vụ Giặt Ủi (`laundry`)

Mã nguồn Spring Boot REST API nằm trong thư mục [`laundry/`](laundry).

---

## 1. Công nghệ & Yêu cầu

- **Java:** 25 (`<java.version>25</java.version>` trong [`laundry/pom.xml`](laundry/pom.xml))
- **Framework:** Spring Boot 4.1.2 (WebMVC, Spring Security, Spring Data JPA, Spring Mail, Thymeleaf)
- **Database:** MySQL / MariaDB (`mysql-connector-j`) khi chạy ứng dụng; H2 in-memory (`MODE=MySQL`) cho bộ test
- **Auth & Tích hợp:** `jjwt` 0.12.6, `java-jwt` 4.4.0, Firebase Admin SDK 9.4.3, Lombok

---

## 2. Cấu hình môi trường (`.env`)

[`application.properties`](laundry/src/main/resources/application.properties) tự động nạp biến môi trường từ `LS-BackEnd/laundry/.env` (qua `spring.config.import`).

Tạo file `LS-BackEnd/laundry/.env` từ [`laundry/.env.example`](laundry/.env.example):

| Nhóm | Biến môi trường | Mặc định / Ghi chú |
|---|---|---|
| **Server & JPA** | `SERVER_PORT`, `JPA_SHOW_SQL`, `JPA_DDL_AUTO`, `APP_LOG_LEVEL` | `8080`, `true`, `update`, `DEBUG` |
| **Database** | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | Ví dụ `jdbc:mysql://localhost:3306/laundry?...` |
| **JWT** | `JWT_SECRET`, `JWT_ACCESS_DURATION`, `JWT_REFRESH_DURATION` | `JWT_SECRET` dài >= 32 ký tự và **trùng với Frontend**; access `1800000` ms (30 phút), refresh `604800000` ms (7 ngày) |
| **CORS** | `CORS_ALLOWED_ORIGINS` | Danh sách origin cách nhau bởi dấu `,` (mặc định `http://localhost:3000,http://127.0.0.1:3000,https://oauth.casso.vn`) |
| **Seed local** | `APP_SEED_ENABLED` | `false` (`true` để tự seed tài khoản & shop mẫu khi DB trống) |
| **Firebase** | `FIREBASE_CONFIG_PATH` | Mặc định `firebase-service-account.json` trong classpath; nếu không có file backend sẽ ghi cảnh báo và bỏ qua `FirebaseAuth` |
| **Thanh toán** | `CASSO_BASE_URL`, `CASSO_POLLING_INTERVAL_MS`, `CASSO_API_KEY`, `VIET_QR_URL`, `BANK_ACCOUNT_HOLDER`, `BANK_ACCOUNT_NUMBER` | Cấu hình VietQR và đối soát giao dịch Casso |
| **Mail SMTP** | `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD` | Mặc định `smtp.gmail.com:587` |

> **Bảo mật:** Không commit `.env` hoặc `firebase-service-account.json`. Toàn bộ `@CrossOrigin` hardcode ở controller đã được gỡ bỏ; CORS được kiểm soát tập trung qua `CORS_ALLOWED_ORIGINS` trong [`SecurityConfig.java`](laundry/src/main/java/com/example/laundry/security/SecurityConfig.java).

---

## 3. Chạy ứng dụng & Chạy Test

### Chạy server local & Swagger UI
- Tài liệu hướng dẫn chi tiết cách test toàn bộ API qua **Swagger UI**, tài khoản mẫu và payload chuẩn xem tại [`laundry/README.md`](laundry/README.md).
- Khi server đang chạy, mở Swagger UI tại: `http://localhost:8080/swagger-ui.html` (OpenAPI JSON: `http://localhost:8080/v3/api-docs`).

```powershell
cd LS-BackEnd\laundry

# Chạy server local (cần MySQL/MariaDB và file .env)
.\mvnw.cmd spring-boot:run

# Chạy toàn bộ test (dùng H2 in-memory trong src/test/resources/application.properties)
.\mvnw.cmd -B -o test
```

---

## 4. Phân quyền & Danh sách API chính

### Public (không cần token)
- `POST /auth/login` — Đăng nhập (SĐT + mật khẩu), trả về `accessToken`, `refreshToken`, `account`
- `POST /auth/google` — Đăng nhập / tự động tạo tài khoản Customer bằng Google (`idToken` Firebase hoặc `googleIdToken`)
- `POST /auth/logout` — Thu hồi token (lưu blacklist trong DB)
- `POST /auth/refresh-token` — Cấp `accessToken` mới từ `refreshToken`
- `POST /auth/register`, `POST /customer/register` — Đăng ký tài khoản khách hàng
- `GET /shops` — Danh sách cửa hàng giặt ủi
- `GET /shop/{shopId}/categories` — Danh mục dịch vụ theo cửa hàng
- `GET /shop/{shopId}/category/{categoryId}/services` — Danh sách dịch vụ theo danh mục

### Authenticated (yêu cầu đăng nhập bất kỳ vai trò)
- `GET /accounts/me` — Lấy thông tin tài khoản hiện tại từ Bearer token
- `GET /api/payments/info/{orderId}` — Lấy thông tin / VietQR thanh toán đơn hàng
- `GET /api/payments/status/{orderId}` — Kiểm tra trạng thái thanh toán đơn hàng

### Customer (`ROLE_CUSTOMER` — `/customer/**`)
- `PUT /customer/update` — Cập nhật hồ sơ khách hàng
- `POST /customer/orders/create` — Tạo đơn giặt ủi mới
- `GET /customer/orders/history` — Lịch sử đơn hàng của khách hàng

### Employee (`ROLE_EMPLOYEE` — `/employee/**`)
- `GET /employee/orders` — Danh sách đơn hàng thuộc cửa hàng nhân viên
- `PUT /employee/{orderId}/status` — Cập nhật trạng thái xử lý đơn hàng

### StoreOwner (`ROLE_STOREOWNER` — `/storeowner/**`)
- Nhân viên: `POST /storeowner/employee/create`, `PUT /storeowner/employee/update`, `DELETE /storeowner/employee/delete`, `GET /storeowner/employees`
- Cửa hàng: `POST /storeowner/shop/create`, `PUT /storeowner/shop/update`, `DELETE /storeowner/shop/delete`, `GET /storeowner/shop`
- Danh mục: `POST /storeowner/category/add`, `PUT /storeowner/category/update`, `DELETE /storeowner/category/delete`
- Dịch vụ: `POST /storeowner/service/add`, `PUT /storeowner/service/update`, `DELETE /storeowner/service/delete`
- Quản lý: `GET /storeowner/manager/service`, `GET /storeowner/manager/order`

### Admin (`ROLE_ADMIN` — `/admin/**`)
- Chủ cửa hàng: `POST /admin/storeowner/create`, `PUT /admin/storeowner/update`, `DELETE /admin/storeowner/delete`, `GET /admin/storeowners`
- Danh sách quản trị: `GET /admin/employees`, `GET /admin/customers`, `GET /admin/shops`
- Đơn hàng: `GET /admin/orders`, `GET /admin/orders/customer/{customerId}`, `GET /admin/orders/status/{status}`
