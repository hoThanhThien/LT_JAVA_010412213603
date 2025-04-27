# LT_JAVA_010412213603
Lập trình Java 2024-2025

Tên đề tài : Phần mềm quản lý giặt ủi online chủ đề số 10

Danh sách thành viên: Thành Thiện, Minh Quang, Minh Chí, Triều Dương, Cao Phi. 


Đăng kí:
vào trang chủ -> mở tab đăng nhập-> chọn đk-> xác thực OTP-> xác nhận đúng thi oke ái thì xn lại.


Đăng nhập:


mở tab đăng nhập nhập thông tin đăng nhập



ADMIN:

dựa vào role ở acess token

sửa tạo, quản lí tất cả






Google sheet: [https://docs.google.com/spreadsheets/d/1P1mIud3xEYwwFXpx0a3xlIqZ4jDERAXeDkaCSHtErfM/edit?usp=sharing](https://docs.google.com/spreadsheets/d/1P1mIud3xEYwwFXpx0a3xlIqZ4jDERAXeDkaCSHtErfM/edit?usp=sharing)

gg meet:[ https://meet.google.com/wxq-kpaa-hps?authuser=2](https://meet.google.com/wxq-kpaa-hps?authuser=2)

Các API :


-CUSTOMER


+Register: POST http://localhost:8080/customer/register


+POST http://localhost:8080/auth/refresh-token


+login: POST http://localhost:8080/auth/login


+logout: POST http://localhost:8080/auth/logout



+POST http://localhost:8080/customer/{{customerId}}/orders  


+GET http://localhost:8080/customer/{{customerId}}/history-order


+lich su order cho khach:GET http://localhost:8080/customer/orders/history?paged=0&size=5


+update customer:PUT http://localhost:8080/accounts/me

  
-ADMIN


+login AMDIN: POST http://localhost:8080/auth/login


+ lich su dat hang cho admin GET http://localhost:8080/admin/orders/history?page=0&size=5


+POST http://localhost:8080/admin/storeowner/create


+DELETE http://localhost:8080/admin/storeowner/delete


+GET http://localhost:8080/admin/storeowners?page={{$random.integer(100)}}&


+GET http://localhost:8080/admin/orders


+GET http://localhost:8080/admin/orders/customer/{{customerId}}


+GET http://localhost:8080/admin/orders/status={{status}}?page=0&size=5



    
-STOREOWNER
+POST http://localhost:8080/admin/storeowner/create


+DELETE http://localhost:8080/storeowner/employee/delete


+PUT http://localhost:8080/storeowner/employee/update


+POST http://localhost:8080/storeowner/shop/create


+POST http://localhost:8080/storeowner/shop/update


+POST http://localhost:8080/storeowner/shop/delete


+POST http://localhost:8080/storeowner/category/add


+POST http://localhost:8080/storeowner/category/update


+POST http://localhost:8080/storeowner/category/delete


+POST http://localhost:8080/storeowner/service/add


+POST http://localhost:8080/storeowner/service/delete


-Category


+GET http://localhost:8080/shop/{{shopId}}/category/{{categoryId}}/services


+GET http://localhost:8080/shop/{{shopId}}/categories


+GET http://localhost:8080/shop/{{shopId}}/category/{{categoryId}}/services


-Shop laundry


+GET http://localhost:8080/shops

-employee

+PUT http://localhost:8080/employee/{{orderId}}/status?status=DONE



