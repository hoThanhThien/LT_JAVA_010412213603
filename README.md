# LT_JAVA_010412213603
Lập trình Java 2024-2025

Tên đề tài : Phần mềm quản lý giặt ủi online chủ đề số 10

Danh sách thành viên: Thành Thiện, Minh Quang, Minh Chí, Triều Dương, Cao Phi. 


Google sheet: [https://docs.google.com/spreadsheets/d/1P1mIud3xEYwwFXpx0a3xlIqZ4jDERAXeDkaCSHtErfM/edit?usp=sharing](https://docs.google.com/spreadsheets/d/1P1mIud3xEYwwFXpx0a3xlIqZ4jDERAXeDkaCSHtErfM/edit?usp=sharing)

gg meet:[ https://meet.google.com/wxq-kpaa-hps?authuser=2](https://meet.google.com/wxq-kpaa-hps?authuser=2)

Các API :
-CUSTOMER
+login: POST http://localhost:8080/auth/login
+ lich su order cho khach:GET http://localhost:8080/customer/orders/history?paged=0&size=5
+ update customer:PUT http://localhost:8080/accounts/me
-ADMIN
+login AMDIN: POST http://localhost:8080/auth/login
+ lich su dat hang cho admin GET http://localhost:8080/admin/orders/history?page=0&size=5
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


-SHop laundry


+GET http://localhost:8080/shops
