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
      laundryShopRepository.findAll().stream()
          .filter(s -> "HERAMO Quận 1".equals(s.getName()))
          .findFirst()
          .ifPresent(this::ensureFullCategoriesForSampleShop);
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
    ensureFullCategoriesForSampleShop(savedShop);

    log.info("Seeded local sample accounts (phone 0900000001..0900000004 / pass 123456) and sample shop.");
  }

  private void ensureFullCategoriesForSampleShop(LaundryShop shop) {
    java.util.List<ServiceCategory> existingCategories =
        serviceCategoryRepository.findByShopId(Math.toIntExact(shop.getId()));

    ServiceCategory washDry = findOrCreateCategory(
        existingCategories, shop, "Giặt sấy",
        "Giặt sấy nhanh giao nhận tận nơi trong 24h", "/service-2.png");
    ensureService(washDry, "Giặt sấy tiêu chuẩn", 35000.0, "Giặt sạch thơm tính theo kg", "24 giờ", "/service-2.png");
    ensureService(washDry, "Ủi xếp / Ủi treo", 30000.0, "Ủi thẳng đồ cao cấp tính theo kg/món", "24 giờ", "/service-2.png");

    ServiceCategory dryClean = findOrCreateCategory(
        existingCategories, shop, "Giặt hấp - Giặt khô",
        "Chăm sóc đồ hiệu, vest, áo dài đúng chuẩn", "/service-1.png");
    ensureService(dryClean, "Giặt hấp Vest cao cấp", 95000.0, "Quy trình giặt khô dung môi chuyên dụng", "48 giờ", "/service-1.png");
    ensureService(dryClean, "Vệ sinh spa túi xách", 150000.0, "Chăm sóc phục hồi túi da cao cấp", "48 giờ", "/service-4.png");

    ServiceCategory shoeCare = findOrCreateCategory(
        existingCategories, shop, "Vệ sinh giày",
        "Vệ sinh, hấp khử mùi UV và phục hồi giày chuyên sâu", "/service-3.png");
    ensureService(shoeCare, "Vệ sinh giày toàn diện", 139000.0, "Vệ sinh thủ công dung dịch cao cấp", "48 giờ", "/service-3.png");
    ensureService(shoeCare, "Vệ sinh giày da / da lộn", 209000.0, "Xử lý chuyên biệt cho chất liệu da", "48 giờ", "/service-3.png");

    ServiceCategory homeCare = findOrCreateCategory(
        existingCategories, shop, "Vệ sinh sofa, nệm, rèm, thảm",
        "Vệ sinh tận nơi bằng dung dịch chuyên dụng an toàn", "/service-5.jpg");
    ensureService(homeCare, "Vệ sinh sofa tiêu chuẩn", 350000.0, "Khử khuẩn sâu, nhanh khô 3-5 giờ", "Trong ngày", "/service-5.jpg");
    ensureService(homeCare, "Vệ sinh nệm / thảm tận nơi", 300000.0, "Làm sạch bụi mịn và vết bẩn cứng đầu", "Trong ngày", "/service-5.jpg");

    ServiceCategory acCare = findOrCreateCategory(
        existingCategories, shop, "Vệ sinh máy lạnh",
        "Vệ sinh dàn lạnh/dàn nóng và kiểm tra bơm ga", "/service-6.png");
    ensureService(acCare, "Vệ sinh máy lạnh treo tường", 220000.0, "Bảo dưỡng sạch sâu giúp tiết kiệm điện", "1 giờ", "/service-6.png");
    ensureService(acCare, "Vệ sinh máy lạnh âm trần / tủ đứng", 600000.0, "Quy trình kỹ thuật chuyên nghiệp tại nhà", "1-2 giờ", "/service-6.png");
  }

  private ServiceCategory findOrCreateCategory(
      java.util.List<ServiceCategory> existingCategories,
      LaundryShop shop,
      String name,
      String description,
      String imageDesc) {
    return existingCategories.stream()
        .filter(c -> name.equals(c.getName()))
        .findFirst()
        .orElseGet(() -> {
          ServiceCategory category = new ServiceCategory();
          category.setName(name);
          category.setDescription(description);
          category.setImageDesc(imageDesc);
          category.setShop(shop);
          return serviceCategoryRepository.save(category);
        });
  }

  private void ensureService(
      ServiceCategory category,
      String name,
      double price,
      String description,
      String estimatedTime,
      String imageDesc) {
    if (!serviceRepository.existsByNameAndCategory(name, category)) {
      Service service = new Service(name, price, description, estimatedTime);
      service.setImageDesc(imageDesc);
      service.setCategory(category);
      serviceRepository.save(service);
    }
  }
}
