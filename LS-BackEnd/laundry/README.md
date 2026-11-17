# Laundry Service API — Hướng dẫn Test API (Swagger UI & REST)

Tài liệu hướng dẫn kiểm thử toàn bộ REST API của **Laundry Service API** (`LS-BackEnd/laundry`) bằng **Swagger UI**, **cURL** hoặc **Postman**.

---

## 1. Khởi động Backend & Mở Swagger UI

1. Đảm bảo **MySQL / MariaDB** đang chạy (cổng mặc định `3306`) và đã cấu hình `LS-BackEnd/laundry/.env` (xem mẫu [`.env.example`](.env.example)).
   - Để có sẵn dữ liệu mẫu phục vụ test, đặt `APP_SEED_ENABLED=true` trong `.env`.
2. Chạy Backend:
   ```powershell
   cd LS-BackEnd\laundry
   .\mvnw.cmd spring-boot:run
   ```
3. Truy cập trên trình duyệt:
   - **Swagger UI:** http://localhost:8080/swagger-ui.html
   - **OpenAPI JSON:** http://localhost:8080/v3/api-docs
   - **OpenAPI YAML:** http://localhost:8080/v3/api-docs.yaml

> **Lưu ý:** Mở thẳng `http://localhost:8080/` không phải là trang lỗi server — đây là REST API server nên đường dẫn gốc `/` sẽ trả về `401 {"message": "Token không hợp lệ"}` theo cấu hình bảo mật trong [`SecurityConfig.java`](src/main/java/com/example/laundry/security/SecurityConfig.java). Hãy mở đúng `/swagger-ui.html`.

---

## 2. Tài khoản mẫu & Quy tắc dữ liệu đầu vào

### Tài khoản seed sẵn (`APP_SEED_ENABLED=true` khi DB chưa có user)
Tất cả tài khoản mẫu dùng chung mật khẩu **`123456`** (khởi tạo bởi [`DataSeeder.java`](src/main/java/com/example/laundry/config/DataSeeder.java)):

| Vai trò | SĐT đăng nhập | Email | Quyền test chính |
|---|---|---|---|
| **Admin** | `0900000001` | `admin@laundry.local` | Quản lý StoreOwner, xem toàn bộ shop/khách/nhân viên/đơn hàng |
| **StoreOwner** | `0900000002` | `storeowner@laundry.local` | Quản lý shop, danh mục, dịch vụ, nhân viên và đơn của shop |
| **Employee** | `0900000003` | `employee@laundry.local` | Xem đơn hàng của shop và cập nhật trạng thái đơn |
| **Customer** | `0900000004` | `customer@laundry.local` | Cập nhật hồ sơ, tạo đơn hàng, xem lịch sử đơn và thanh toán |

Dữ liệu cửa hàng mẫu đi kèm: shop **HERAMO Quận 1**, 2 danh mục (`Giặt sấy`, `Giặt hấp - Giặt khô`) và 2 dịch vụ (`Giặt sấy tiêu chuẩn` - `35000.0`, `Giặt hấp Vest cao cấp` - `95000.0`).

### Quy tắc Validate cần nhớ khi tạo/cập nhật dữ liệu mới
- **Số điện thoại** ([`UserValidator.java`](src/main/java/com/example/laundry/utils/UserValidator.java)): Bắt buộc đúng **10 chữ số** (`^\d{10}$`), ví dụ `0987654321`.
- **Mật khẩu mới**: Tối thiểu **8 ký tự**, phải chứa **chữ cái, số và ký tự đặc biệt** trong nhóm `@#$%^&+=!*._?-`, ví dụ `Test@1234` *(riêng tài khoản seed sẵn dùng `123456`)*.
- **Email**: Đúng định dạng email hợp lệ và không trùng giữa các bảng user.
- **Phân trang** ([`PaginationUtils.java`](src/main/java/com/example/laundry/utils/PaginationUtils.java)): `page >= 0`, `1 <= size <= 100`. Nếu truyền ngoài khoảng này sẽ trả HTTP `400`.
- **Trạng thái đơn hàng (`OrderStatus`)**: Chỉ nhận `IN_PROGRESS` hoặc `DONE`.

---

## 3. Cách gắn JWT Token trên Swagger UI (`Authorize`)

1. Mở mục **`auth-controller`** -> **`POST /auth/login`** -> bấm **Try it out**.
2. Nhập body đăng nhập (ví dụ Admin):
   ```json
   {
     "phone": "0900000001",
     "password": "123456"
   }
   ```
3. Bấm **Execute** -> copy giá trị `data.accessToken` trong response.
4. Cuộn lên đầu trang Swagger UI, bấm nút **Authorize** (góc trên bên phải):
   - Dán trực tiếp chuỗi `accessToken` vào ô **Value** *(không cần gõ thêm tiền tố `Bearer ` vì Swagger tự thêm)*.
   - Bấm **Authorize** -> **Close**.
5. Khi muốn **đổi sang vai trò khác** (ví dụ từ `Admin` sang `Customer`):
   - Bấm lại **Authorize** -> **Logout**, sau đó gọi `POST /auth/login` với SĐT của vai trò mới và gắn lại token mới.

*(Ứng dụng đã bật `springdoc.swagger-ui.persist-authorization=true`, nên token vẫn được giữ lại nếu bạn F5 trang Swagger UI.)*

---

## 4. Kịch bản Test theo từng nhóm API

### Nhóm 1: Auth & Account (`/auth/**`, `/accounts/me`)
| Method | Endpoint | Yêu cầu Auth | Body / Ghi chú |
|---|---|---|---|
| `POST` | `/auth/login` | Không | `{ "phone": "0900000004", "password": "123456" }` -> trả `accessToken`, `refreshToken`, `account` |
| `GET` | `/accounts/me` | Có | Trả về thông tin tài khoản đang đăng nhập theo Bearer token |
| `POST` | `/auth/refresh-token` | Không | `{ "refreshToken": "<refreshToken>" }` -> cấp `accessToken` mới (không dùng `accessToken` cho endpoint này và ngược lại) |
| `POST` | `/auth/logout` | Tuỳ chọn | `{ "refreshToken": "<refreshToken>", "accessToken": "<accessToken>" }` -> đưa token vào blacklist DB |
| `POST` | `/auth/register` hoặc `/customer/register` | Không | `{ "idToken": "<firebase-id-token>", "username": "...", "phone": "...", "password": "..." }` *(Lưu ý: cần cấu hình `firebase-service-account.json`; nếu chưa có sẽ báo thiếu cấu hình Firebase Admin)* |

---

### Nhóm 2: Public Catalog (Không cần đăng nhập)
Nên chạy 3 API này đầu tiên để lấy đúng `shopId`, `categoryId`, `serviceId` đang có trong DB:
1. `GET /shops` -> Lấy `id` của cửa hàng (ví dụ `1`).
2. `GET /shop/{shopId}/categories` -> Lấy `id` của danh mục dịch vụ thuộc shop.
3. `GET /shop/{shopId}/category/{categoryId}/services` -> Lấy `id` của dịch vụ cụ thể.

---

### Nhóm 3: Luồng Customer (`ROLE_CUSTOMER` — đăng nhập `0900000004` / `123456`)
1. **Tạo đơn hàng (`POST /customer/orders/create`)**:
   > Lưu ý cấu trúc [`OrderDTO.java`](src/main/java/com/example/laundry/dto/OrderDTO.java): `laundryShop`, `serviceCategory`, `service` là **object chứa `id`**, không phải số nguyên phẳng. Thay `id` theo kết quả lấy được ở Nhóm 2:
   ```json
   {
     "address": "123 Nguyễn Trãi, Q.1, TP. HCM",
     "imgProduct": "/order-sample.png",
     "laundryShop": { "id": 1 },
     "serviceCategory": { "id": 1 },
     "service": { "id": 1 },
     "orderVolume": 3.5,
     "instructions": "Giặt riêng đồ trắng"
   }
   ```
   - Backend tự tính `totalAmount = orderVolume * service.price`, đặt `orderStatus = IN_PROGRESS`, `paymentStatus = PENDING` và trả về `data.id` (`orderId`).
2. **Xem lịch sử đơn (`GET /customer/orders/history`)**:
   - Query params: `paged=0` (hoặc `page=0`), `size=10`.
3. **Cập nhật hồ sơ (`PUT /customer/update`)**:
   ```json
   {
     "username": "Khách Hàng Test",
     "address": "456 Lê Lợi, Q.1, TP. HCM"
   }
   ```

---

### Nhóm 4: Thanh toán (`/api/payments/**`)
- Yêu cầu đăng nhập (`Admin`, `Customer` sở hữu đơn, hoặc `StoreOwner`/`Employee` thuộc cửa hàng của đơn):
  - `GET /api/payments/info/{orderId}` — Lấy thông tin thanh toán & URL VietQR.
  - `GET /api/payments/status/{orderId}` — Kiểm tra trạng thái thanh toán (`PENDING` / `PAID`...).

---

### Nhóm 5: Luồng Employee (`ROLE_EMPLOYEE` — đăng nhập `0900000003` / `123456`)
1. **Xem danh sách đơn của cửa hàng (`GET /employee/orders`)**: `page=0`, `size=10`.
2. **Cập nhật trạng thái đơn (`PUT /employee/{orderId}/status`)**:
   - Path param: `orderId` (ví dụ `1`)
   - Query param: `status` (`IN_PROGRESS` hoặc `DONE`)

---

### Nhóm 6: Luồng StoreOwner (`ROLE_STOREOWNER` — đăng nhập `0900000002` / `123456`)
1. **Xem cửa hàng của mình (`GET /storeowner/shop`)**
   - Nếu là chủ cửa hàng mới chưa có shop, tạo bằng `POST /storeowner/shop/create`:
     ```json
     {
       "name": "Tiệm Giặt Sạch Q3",
       "address": "78 Võ Văn Tần, Q.3, TP. HCM",
       "openingHours": "07:30 - 21:30",
       "description": "Nhận giao tận nơi"
     }
     ```
   - Cập nhật: `PUT /storeowner/shop/update`
   - Xóa (chỉ xóa được khi chưa có đơn hàng liên quan): `DELETE /storeowner/shop/delete?id=<shopId>`
2. **Quản lý danh mục dịch vụ**:
   - Thêm (`POST /storeowner/category/add`) — bắt buộc có `name`, `description`, `imageDesc`:
     ```json
     {
       "name": "Giặt chăn ga",
       "description": "Giặt sấy chăn mền khổ lớn",
       "imageDesc": "/service-3.png"
     }
     ```
   - Cập nhật (`PUT /storeowner/category/update`): truyền kèm `id` của danh mục.
   - Xóa (`DELETE /storeowner/category/delete?id=<categoryId>`): chỉ xóa được khi không còn dịch vụ bên trong.
3. **Quản lý dịch vụ**:
   - Thêm (`POST /storeowner/service/add`) — bắt buộc có `categoryId`, `name`, `price`, `imageDesc`:
     ```json
     {
       "categoryId": 1,
       "name": "Giặt nhanh 4 giờ",
       "description": "Giao nhận hỏa tốc trong ngày",
       "estimatedTime": "4 giờ",
       "imageDesc": "/service-fast.png",
       "price": 55000
     }
     ```
   - Cập nhật (`PUT /storeowner/service/update`): truyền kèm `id` của dịch vụ.
   - Xóa (`DELETE /storeowner/service/delete?id=<serviceId>`): chỉ xóa được khi chưa có đơn hàng gắn với dịch vụ đó.
4. **Quản lý nhân viên**:
   - Tạo (`POST /storeowner/employee/create`):
     ```json
     {
       "username": "Nhân Viên Mới",
       "password": "Emp@1234",
       "email": "emp.new@laundry.local",
       "phone": "0912345678",
       "address": "Quận 1, TP. HCM"
     }
     ```
   - Danh sách: `GET /storeowner/employees?page=0&size=10`
   - Cập nhật: `PUT /storeowner/employee/update` (nhận diện qua `id` / `email` / `phone` / `username`)
   - Xóa: `DELETE /storeowner/employee/delete?phone=0912345678` (hoặc `email`, `username`)
5. **Tổng hợp quản lý shop**:
   - `GET /storeowner/manager/service?page=0&size=10`
   - `GET /storeowner/manager/order?page=0&size=10`

---

### Nhóm 7: Luồng Admin (`ROLE_ADMIN` — đăng nhập `0900000001` / `123456`)
1. **Quản lý StoreOwner**:
   - Tạo (`POST /admin/storeowner/create`):
     ```json
     {
       "username": "Chủ Cửa Hàng Mới",
       "password": "Owner@1234",
       "email": "owner.new@laundry.local",
       "phone": "0923456789",
       "address": "Quận 3, TP. HCM"
     }
     ```
   - Danh sách: `GET /admin/storeowners?page=0&size=10`
   - Cập nhật: `PUT /admin/storeowner/update` (truyền kèm `id` / `email` / `phone` / `username` để tìm đối tượng cần sửa)
   - Xóa: `DELETE /admin/storeowner/delete?phone=0923456789` (hoặc truyền query `id`/`email`/`username` hoặc JSON body)
2. **Thống kê & Tra cứu toàn hệ thống**:
   - `GET /admin/employees?page=0&size=10`
   - `GET /admin/customers?page=0&size=10`
   - `GET /admin/shops?page=0&size=10`
   - `GET /admin/orders?page=0&size=10`
   - `GET /admin/orders/customer/{customerId}?page=0&size=5` (`customerId` là UUID)
   - `GET /admin/orders/status/{status}?page=0&size=10` (`status` là `IN_PROGRESS` hoặc `DONE`)

---

## 5. Ví dụ test nhanh bằng cURL (PowerShell)

```powershell
# 1. Đăng nhập lấy accessToken
$login = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/auth/login" `
  -ContentType "application/json" `
  -Body '{"phone":"0900000001","password":"123456"}'
$token = $login.data.accessToken

# 2. Gọi API cần quyền Admin
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/admin/customers?page=0&size=10" `
  -Headers @{ Authorization = "Bearer $token" }
```

---

## 6. Xử lý lỗi thường gặp khi test

| HTTP Status / Thông báo | Nguyên nhân | Cách khắc phục |
|---|---|---|
| `401 {"message": "Token không hợp lệ"}` | Chưa bấm **Authorize** hoặc token hết hạn | Đăng nhập lại qua `/auth/login` rồi dán `accessToken` mới vào **Authorize** |
| `401 {"message": "Token đã bị thu hồi"}` | Token vừa dùng đã bị `POST /auth/logout` | Đăng nhập lại lấy token mới |
| `403 {"message": "Truy cập bị từ chối"}` | Đang dùng token của vai trò khác (ví dụ Customer gọi `/admin/**`) | Bấm **Authorize -> Logout** và đăng nhập đúng vai trò |
| `400` khi gọi API danh sách | Truyền `page < 0` hoặc `size` ngoài `[1..100]` | Đặt `page >= 0`, `1 <= size <= 100` |
| `Chưa cấu hình Firebase Admin` khi `/auth/register` | Thiếu `firebase-service-account.json` ở local | Dùng tài khoản mẫu của `DataSeeder` hoặc thêm file Firebase Admin JSON |

---

## 7. Tắt Swagger ở môi trường Production (tùy chọn)

Khi triển khai production, có thể tắt Swagger trong cấu hình hoặc biến môi trường:

```properties
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```
