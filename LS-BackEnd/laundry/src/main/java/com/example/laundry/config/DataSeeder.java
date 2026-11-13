package com.example.laundry.config;

import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.shop.ServiceCategory;
import com.example.laundry.models.user.Admin;
import com.example.laundry.models.user.Customer;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.AdminRepository;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.repository.ServiceCategoryRepository;
import com.example.laundry.repository.ServiceRepository;
import com.example.laundry.repository.StoreOwnerRepository;
import com.example.laundry.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Seeds sample local-development data when the database has no users yet.
 * Guarded by app.seed.enabled=true so production and automated tests stay isolated.
 */
@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

  private final UserRepository userRepository;
  private final AdminRepository adminRepository;
  private final StoreOwnerRepository storeOwnerRepository;
  private final EmployeeRepository employeeRepository;
  private final CustomerRepository customerRepository;
  private final LaundryShopRepository laundryShopRepository;
  private final ServiceCategoryRepository serviceCategoryRepository;
  private final ServiceRepository serviceRepository;
  private final PasswordEncoder passwordEncoder;

  public DataSeeder(
      UserRepository userRepository,
      AdminRepository adminRepository,
      StoreOwnerRepository storeOwnerRepository,
      EmployeeRepository employeeRepository,
      CustomerRepository customerRepository,
      LaundryShopRepository laundryShopRepository,
      ServiceCategoryRepository serviceCategoryRepository,
      ServiceRepository serviceRepository,
      PasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
    this.adminRepository = adminRepository;
    this.storeOwnerRepository = storeOwnerRepository;
    this.employeeRepository = employeeRepository;
    this.customerRepository = customerRepository;
    this.laundryShopRepository = laundryShopRepository;
    this.serviceCategoryRepository = serviceCategoryRepository;
    this.serviceRepository = serviceRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void run(String... args) {
    if (userRepository.count() > 0) {
      return;
    }

    String defaultPassword = passwordEncoder.encode("123456");

    Admin admin = new Admin(
        "Admin Hệ Thống",
        defaultPassword,
        "admin@laundry.local",
        "0900000001",
        "Quận 1, TP. HCM",
        Roles.Admin
    );
    adminRepository.save(admin);

    StoreOwner storeOwner = new StoreOwner();
    storeOwner.setUsername("Chủ Cửa Hàng Mẫu");
    storeOwner.setPassword(defaultPassword);
    storeOwner.setEmail("storeowner@laundry.local");
    storeOwner.setPhone("0900000002");
    storeOwner.setAddress("123 Lê Lợi, Q.1, TP. HCM");
    storeOwner.setRoles(Roles.StoreOwner);
    StoreOwner savedOwner = storeOwnerRepository.save(storeOwner);

    Employee employee = new Employee();
    employee.setUsername("Nhân Viên Mẫu");
    employee.setPassword(defaultPassword);
    employee.setEmail("employee@laundry.local");
    employee.setPhone("0900000003");
    employee.setAddress("123 Lê Lợi, Q.1, TP. HCM");
    employee.setRoles(Roles.Employee);
    employee.setStoreOwner(savedOwner);
    employeeRepository.save(employee);

    Customer customer = new Customer(
        "Khách Hàng Mẫu",
        defaultPassword,
        "customer@laundry.local",
        "0900000004",
        "456 Nguyễn Huệ, Q.1, TP. HCM",
        Roles.Customer
    );
    customerRepository.save(customer);

    LaundryShop shop = new LaundryShop();
    shop.setName("HERAMO Quận 1");
    shop.setAddress("123 Lê Lợi, Q.1, TP. HCM");
    shop.setOpeningHours("08:00 - 21:00");
    shop.setDescription("Cửa hàng giặt ủi mẫu cho môi trường local");
    shop.setAverageRating(4.9);
    shop.setCreatedAt(new Date());
    shop.setStoreOwner(savedOwner);
    LaundryShop savedShop = laundryShopRepository.save(shop);

    ServiceCategory washDry = new ServiceCategory();
    washDry.setName("Giặt sấy");
    washDry.setDescription("Giặt sấy nhanh giao nhận tận nơi trong 24h");
    washDry.setImageDesc("/service-2.png");
    washDry.setShop(savedShop);
    ServiceCategory savedWashDry = serviceCategoryRepository.save(washDry);

    ServiceCategory dryClean = new ServiceCategory();
    dryClean.setName("Giặt hấp - Giặt khô");
    dryClean.setDescription("Chăm sóc đồ hiệu, vest, áo dài đúng chuẩn");
    dryClean.setImageDesc("/service-1.png");
    dryClean.setShop(savedShop);
    ServiceCategory savedDryClean = serviceCategoryRepository.save(dryClean);

    Service s1 = new Service("Giặt sấy tiêu chuẩn", 35000.0, "Giặt sạch thơm tính theo kg", "24 giờ");
    s1.setImageDesc("/service-2.png");
    s1.setCategory(savedWashDry);
    serviceRepository.save(s1);

    Service s2 = new Service("Giặt hấp Vest cao cấp", 95000.0, "Quy trình giặt khô dung môi chuyên dụng", "48 giờ");
    s2.setImageDesc("/service-1.png");
    s2.setCategory(savedDryClean);
    serviceRepository.save(s2);

    log.info("Seeded local sample accounts (phone 0900000001..0900000004 / pass 123456) and sample shop.");
  }
}
