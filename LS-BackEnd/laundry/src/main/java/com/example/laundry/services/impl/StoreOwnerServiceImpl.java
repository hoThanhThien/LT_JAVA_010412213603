package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.shop.ServiceCategory;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.Roles;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.*;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.services.OrderService;
import com.example.laundry.services.StoreOwnerService;
import com.example.laundry.utils.ApiResponse;
import com.example.laundry.utils.UserValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class StoreOwnerServiceImpl implements StoreOwnerService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LaundryShopRepository laundryShopRepository;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    // Lấy store owner hiện tại
    private StoreOwner getCurrentStoreOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return storeOwnerRepository.findByUsername(currentUsername);
    }

    @Override
    public void addStoreOwner(StoreOwner storeOwner) {
        // Thực hiện logic thêm store owner
        storeOwnerRepository.save(storeOwner);
    }

    @Override
    public void deleteStoreOwner(StoreOwner storeOwner) {
        // Thực hiện logic xóa store owner
        storeOwnerRepository.delete(storeOwner);
    }

    @Override
    public ApiResponse<EmployeeDTO> createEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        // Kiểm tra dữ liệu
        if (employeeDTO.getPassword() == null || employeeDTO.getPassword().isEmpty()) {
            return new ApiResponse<>("Mật khẩu không được để trống", null);
        }

        // Kiểm tra password
        if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
            return new ApiResponse<>("Mật khẩu không hợp lệ!!!", null);
        }

        // Kiểm tra email
        if (!UserValidator.isValidEmail(employeeDTO.getEmail())) {
            return new ApiResponse<>("Email không hợp lệ!!!", null);
        }

        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            return new ApiResponse<>("Email đã tồn tại!!!", null);
        }

        // Kiểm tra phone
        if (!UserValidator.isValidPhone(employeeDTO.getPhone())) {
            return new ApiResponse<>("Số điện thoại phải có 10 chữ số!!!", null);
        }

        if (employeeRepository.existsByPhone(employeeDTO.getPhone())) {
            return new ApiResponse<>("Số điện thoại đã tồn tại!!!", null);
        }

        //Kiểm tra xem StoreOwner có shop chưa
        LaundryShop laundryShop = laundryShopRepository.findByStoreOwner(storeOwner);
        if (laundryShop == null) {
            return new ApiResponse<>("StoreOwner chưa có shop, không thể tạo nhân viên", null);
        }

        // Mã hóa password trước khi lưu xuống db
        String encodedPassword = passwordEncoder.encode(employeeDTO.getPassword());

        // Tạo đối tượng Employee và thiết lập mối quan hệ với StoreOwner
        Employee employee = new Employee(
                employeeDTO.getUsername(),
                encodedPassword,
                employeeDTO.getEmail(),
                employeeDTO.getPhone(),
                employeeDTO.getAddress(),
                Roles.Employee,
                employeeDTO.getAvtUser(),
                employeeDTO.getCreatedAt(),
                employeeDTO.getUpdatedAt(),
                storeOwner
        );

        //Gán shop vào employee
        employee.setShop(laundryShop);

        Employee savedEmployee = employeeService.addEmployee(employee); // trả về object sau khi save

        EmployeeDTO responseDTO = new EmployeeDTO(savedEmployee); // dùng constructor convert từ entity

        responseDTO.setRole(savedEmployee.getRoles());
        responseDTO.setAvtUser(savedEmployee.getAvtUser());
        responseDTO.setCreatedAt(savedEmployee.getCreatedAt());
        responseDTO.setUpdatedAt(savedEmployee.getUpdatedAt());

        return new ApiResponse<>("Tạo tài khoản nhân viên thành công", responseDTO);

    }

    @Override
    public ApiResponse<String> deleteEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        // Kiểm tra xem tồn tại hay không
        if (employeeDTO.getEmail() == null && employeeDTO.getPhone() == null && employeeDTO.getUsername() == null) {
            return new ApiResponse<>("Không đủ thông tin để xóa!!!", null);
        }

        // Tìm Employee theo các thông tin cung cấp
        Employee employee = findEmployeeByInfo(employeeDTO);

        if (employee == null) {
            return new ApiResponse<>("Không tìm thấy nhân viên với thông tin đã cung cấp!!!", null);
        }

        // Kiểm tra Employee có thuộc về StoreOwner hiện tại không
        if (employee.getStoreOwner() == null || !employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return new ApiResponse<>("Bạn không có quyền xóa nhân viên này", null);
        }

        employeeService.deleteEmployee(employee);

        return new ApiResponse<>("Đã xóa nhân viên thành công", null);
    }

    @Override
    @Transactional
    public ApiResponse<EmployeeDTO> updateEmployee(StoreOwner storeOwner, EmployeeDTO employeeDTO) {
        Employee employee = findEmployeeByInfo(employeeDTO);
        if (employee == null) {
            return new ApiResponse<>("Không tìm thấy nhân viên với thông tin đã cung cấp!", null);
        }

        if (!employee.getStoreOwner().getId().equals(storeOwner.getId())) {
            return new ApiResponse<>("Bạn không có quyền cập nhật nhân viên này!", null);
        }

        if (employeeDTO.getUsername() != null && !employeeDTO.getUsername().isEmpty()) {
            employee.setUsername(employeeDTO.getUsername());
        }

        if (employeeDTO.getAddress() != null) {
            employee.setAddress(employeeDTO.getAddress());
        }

        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
            if (!UserValidator.isValidPassword(employeeDTO.getPassword())) {
                return new ApiResponse<>("Mật khẩu không hợp lệ!", null);
            }

            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }

        if (employeeDTO.getAvtUser() != null && employeeDTO.getAvtUser().isEmpty()) {
            employee.setAvtUser(employeeDTO.getAvtUser());
        }

        Employee updatedEmployee = employeeRepository.save(employee);

        EmployeeDTO responseDTO = new EmployeeDTO();
        responseDTO.setUsername(updatedEmployee.getUsername());
        responseDTO.setEmail(updatedEmployee.getEmail());
        responseDTO.setPhone(updatedEmployee.getPhone());
        responseDTO.setAddress(updatedEmployee.getAddress());
        responseDTO.setAvtUser(updatedEmployee.getAvtUser());
        responseDTO.setCreatedAt(updatedEmployee.getCreatedAt());
        responseDTO.setUpdatedAt(updatedEmployee.getUpdatedAt());

        return new ApiResponse<>("Cập nhật nhân viên thành công!", responseDTO);
    }

    @Override
    public ApiResponse<LaundryShopDTO> createShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO) {
        //Kiểm tra xem trước đó đã quản lý shop nào chưa
        if(laundryShopRepository.existsByStoreOwner(storeOwner)) {
            return new ApiResponse<>("Bạn đã có cửa hàng rồi, không được tạo thêm");
        }

        // kiểm tra dữ liệu
        if(laundryShopRepository.existsByName(laundryShopDTO.getName())) {
            return new ApiResponse<>("Tên cửa hàng đã tồn tại vui lòng chọn tên khác!!!");
        }

        if(laundryShopDTO.getName() == null || laundryShopDTO.getName().isEmpty()) {
            return new ApiResponse<>("Tên cửa hàng không được để trống");
        }

        if(laundryShopDTO.getAddress() == null || laundryShopDTO.getAddress().isEmpty()) {
            return new ApiResponse<>("Địa chỉ cửa hàng không được để trống");
        }

        LaundryShop laundryShop = new LaundryShop(
                laundryShopDTO.getId(),
                laundryShopDTO.getName(),
                laundryShopDTO.getAddress(),
                laundryShopDTO.getOpeningHours(),
                laundryShopDTO.getDescription(),
                laundryShopDTO.getAverageRating(),
                storeOwner
        );

        // Lưu shop vào database
        LaundryShop createdShop = laundryShopRepository.save(laundryShop);

        LaundryShopDTO responseDTO = convertToLaundryDTO(createdShop);

        return new ApiResponse<>("Tạo cửa hàng thành công!!!", responseDTO);
    }

    @Override
    public ApiResponse<LaundryShopDTO> getLaundryShopByStoreOwner(StoreOwner storeOwner) {
      LaundryShop laundryShop = laundryShopRepository.findByStoreOwner(storeOwner);
      if(laundryShop == null) {
        return new ApiResponse<>("Bạn chưa có cửa hàng!!!!");
      }

      LaundryShopDTO responseDTO = convertToLaundryDTO(laundryShop);
      responseDTO.setId(laundryShop.getId());
      responseDTO.setName(laundryShop.getName());
      responseDTO.setAddress(laundryShop.getAddress());
      responseDTO.setOpeningHours(laundryShop.getOpeningHours());
      responseDTO.setDescription(laundryShop.getDescription());
      responseDTO.setAverageRating(laundryShop.getAverageRating());

      return new ApiResponse<>("Lây ra cửa hàng của bạn thành công!!!", responseDTO);
    }

    @Override
    public ApiResponse<LaundryShopDTO> updateLaundryShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO) {
        LaundryShop existingShop =  laundryShopRepository.findByStoreOwner(storeOwner);
        if(existingShop == null) {
            return new ApiResponse<>("Bạn chưa có cửa hàng để cập nhật!!!", null);
        }

        if (laundryShopDTO.getName() != null &&
                !laundryShopDTO.getName().equals(existingShop.getName()) &&
                laundryShopRepository.existsByName(laundryShopDTO.getName())) {
            return new ApiResponse<>("Tên cửa hàng đã tồn tại, vui lòng chọn tên khác!", null);
        }

        if (laundryShopDTO.getName() != null) existingShop.setName(laundryShopDTO.getName());
        if (laundryShopDTO.getAddress() != null) existingShop.setAddress(laundryShopDTO.getAddress());
        if (laundryShopDTO.getOpeningHours() != null) existingShop.setOpeningHours(laundryShopDTO.getOpeningHours());
        if (laundryShopDTO.getDescription() != null) existingShop.setDescription(laundryShopDTO.getDescription());

        LaundryShop updatedShop = laundryShopRepository.save(existingShop);
        LaundryShopDTO responseDTO = convertToLaundryDTO(updatedShop);

        return new ApiResponse<>("Cập nhật cửa hàng thành công!!!", responseDTO);
    }

    @Override
    public ApiResponse<String> deleteLaundryShop(StoreOwner storeOwner, LaundryShopDTO laundryShopDTO) {
        // Kiểm tra xem tồn tại không
        if(laundryShopDTO.getName() == null || laundryShopDTO.getName().isEmpty()) {
            return new ApiResponse<>("Không đủ thông tin để xóa!!!", null);
        }

        LaundryShop laundryShop = laundryShopRepository.findByStoreOwner(storeOwner);

        if(laundryShop == null) {
            return new ApiResponse<>("Không tìm thấy cửa hàng với thông tin đã cung cấp!!!", null);
        }

        laundryShopRepository.delete(laundryShop);

        return new ApiResponse<>("Đã xóa cửa hàng thành công", null);
    }

    @Override
    public ApiResponse<ServiceCategoryDTO> createServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO) {
        LaundryShop existingShop =  laundryShopRepository.findByStoreOwner(storeOwner);

        //Kiểm tra dữ liệu
        if (serviceCategoryRepository.existsByNameAndShop(serviceCategoryDTO.getName(), existingShop)) {
            return new ApiResponse<>("Tên mục dịch vụ đã tồn tại trong cửa hàng!", null);
        }

        if(serviceCategoryDTO.getName() == null) {
            return new ApiResponse<>("Tên mục dịch vụ không được để trống!!!");
        }

        if(serviceCategoryDTO.getDescription() == null) {
            return new ApiResponse<>("Thiếu phần mô tả!!!");
        }

        if(serviceCategoryDTO.getImageDesc() == null) {
            return new ApiResponse<>("Thiếu ảnh mô tả!!!");
        }

        ServiceCategory serviceCategory = new ServiceCategory(
                serviceCategoryDTO.getId(),
                serviceCategoryDTO.getName(),
                serviceCategoryDTO.getDescription(),
                serviceCategoryDTO.getImageDesc()
        );

        serviceCategory.setShop(existingShop);

        ServiceCategory savedCategory = serviceCategoryRepository.save(serviceCategory);

        ServiceCategoryDTO responseDTO = new ServiceCategoryDTO(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription(),
                savedCategory.getImageDesc()
        );

        return new ApiResponse<>("Thêm mục dịch vụ thành công!!!", responseDTO);
    }

    @Override
    public ApiResponse<ServiceCategoryDTO> updateServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO) {
        //Kiểm tra dữ liệu
        ServiceCategory exstingServiceCategory = serviceCategoryRepository.findById(serviceCategoryDTO.getId()).get();
        if(serviceCategoryDTO.getId() == null) {
            return new ApiResponse<>("Bạn chưa có mục dịch vụ để cập nhật!!!", null);
        }

        if (serviceCategoryDTO.getName() != null &&
                !serviceCategoryDTO.getName().equals(exstingServiceCategory.getName()) &&
                serviceCategoryRepository.existsByNameAndShopAndIdNot(serviceCategoryDTO.getName(), exstingServiceCategory.getShop(), exstingServiceCategory.getId())) {

            return new ApiResponse<>("Tên mục dịch vụ đã tồn tại trong cửa hàng!", null);
        }


        if(serviceCategoryDTO.getName() != null) exstingServiceCategory.setName(serviceCategoryDTO.getName());
        if(serviceCategoryDTO.getDescription() != null) exstingServiceCategory.setDescription(serviceCategoryDTO.getDescription());
        if(serviceCategoryDTO.getImageDesc() != null) exstingServiceCategory.setImageDesc(serviceCategoryDTO.getImageDesc());
        if(serviceCategoryDTO.getDescription() != null) exstingServiceCategory.setDescription(serviceCategoryDTO.getDescription());

        ServiceCategory updatedService = serviceCategoryRepository.save(exstingServiceCategory);

        ServiceCategoryDTO responseDTO = convertToServiceCategoryDTO(updatedService);

        return new ApiResponse<>("Cập nhật mục dịch vụ thành công!!!", responseDTO);
    }

    @Override
    public ApiResponse<Integer> deleteServiceCategory(StoreOwner storeOwner, ServiceCategoryDTO serviceCategoryDTO) {
        // Kiểm tra dữ liệu
        if(serviceCategoryDTO.getId() == null) {
            return new ApiResponse<>("Không đủ thông tin để xóa!!!", null);
        }

        Optional<ServiceCategory> serviceCategory = serviceCategoryRepository.findById(serviceCategoryDTO.getId());

        if(serviceCategory.isEmpty()) {
            return new ApiResponse<>("Không tìm thấy dịch vụ với thông tin đã cung cấp!!!", null);
        }

        serviceCategoryRepository.delete(serviceCategory.get());

        return new ApiResponse<>("Đã xóa mục dịch vụ thành công", null);
    }

    @Override
    public ApiResponse<ServiceDTO> createService(StoreOwner storeOwner, ServiceDTO serviceDTO) {
        LaundryShop existingShop =  laundryShopRepository.findByStoreOwner(storeOwner);

      // Kiểm tra categoryId
      if (serviceDTO.getCategoryId() == null) {
        return new ApiResponse<>("Thiếu thông tin mục dịch vụ (categoryId)!!!", null);
      }

      Optional<ServiceCategory> optionalCategory = serviceCategoryRepository.findById(serviceDTO.getCategoryId());

      if (optionalCategory.isEmpty()) {
        return new ApiResponse<>("Không tìm thấy mục dịch vụ với categoryId đã cung cấp!", null);
      }

      ServiceCategory serviceCategory = optionalCategory.get();

      // Kiểm tra xem category đó có thuộc shop của StoreOwner không
      if (!serviceCategory.getShop().getId().equals(existingShop.getId())) {
        return new ApiResponse<>("Mục dịch vụ không thuộc cửa hàng của bạn!", null);
      }

      // Kiểm tra tên dịch vụ có trùng không
      if (serviceRepository.existsByNameAndCategory(serviceDTO.getName(), serviceCategory)) {
        return new ApiResponse<>("Tên dịch vụ đã tồn tại trong mục dịch vụ!", null);
      }

      // Kiểm tra dữ liệu cơ bản
      if (serviceDTO.getName() == null || serviceDTO.getName().isEmpty()) {
        return new ApiResponse<>("Tên dịch vụ không được để trống!!!");
      }

      if (serviceDTO.getPrice() == null) {
        return new ApiResponse<>("Giá không được để trống!!!");
      }

      if (serviceDTO.getImageDesc() == null) {
        return new ApiResponse<>("Thiếu ảnh mô tả!!!");
      }

      // Tạo service
      Service service = new Service(
              serviceDTO.getId(),
              serviceDTO.getName(),
              serviceDTO.getDescription(),
              serviceDTO.getEstimatedTime(),
              serviceDTO.getPrice(),
              serviceDTO.getImageDesc()
      );
      service.setCategory(serviceCategory);

      Service savedService = serviceRepository.save(service);

      // Response
      ServiceDTO responseDTO = new ServiceDTO(
              savedService.getId(),
              savedService.getName(),
              savedService.getDescription(),
              savedService.getEstimatedTime(),
              savedService.getImageDesc(),
              savedService.getPrice()
      );
      responseDTO.setCategoryId(serviceCategory.getId());

      return new ApiResponse<>("Thêm dịch vụ thành công!!!", responseDTO);
    }

    @Override
    public ApiResponse<ServiceDTO> updateService(StoreOwner storeOwner, ServiceDTO serviceDTO) {
        // Kiểm tra dữ liệu
        Service exstingService = serviceRepository.findById(serviceDTO.getId()).get();
        if(serviceDTO.getId() == null) {
            return new ApiResponse<>("Bạn chưa có dịch vụ để cập nhật!!!", null);
        }

        if (serviceDTO.getName() != null &&
                !serviceDTO.getName().equals(exstingService.getName()) &&
                serviceRepository.existsByNameAndCategory_ShopAndIdNot(serviceDTO.getName(), exstingService.getCategory().getShop(), exstingService.getId())) {

            return new ApiResponse<>("Tên dịch vụ đã tồn tại trong cửa hàng!", null);
        }

        if(serviceDTO.getName() != null) exstingService.setName(serviceDTO.getName());
        if(serviceDTO.getDescription() != null) exstingService.setDescription(serviceDTO.getDescription());
        if(serviceDTO.getPrice() != null) exstingService.setPrice(serviceDTO.getPrice());
        if(serviceDTO.getImageDesc() != null) exstingService.setImageDesc(serviceDTO.getImageDesc());
        if(serviceDTO.getEstimatedTime() != null) exstingService.setEstimatedTime(serviceDTO.getEstimatedTime());
        
        Service updatedService = serviceRepository.save(exstingService);

        ServiceDTO responseDTO = convertToServiceDTO(updatedService);
        
        return new ApiResponse<>("Cập nhật dịch vụ thành công!!!", responseDTO);
    }

    @Override
    public ApiResponse<Integer> deleteService(StoreOwner storeOwner, ServiceDTO serviceDTO) {
        // Kiểm tra dữ liệu
        if(serviceDTO.getId() == null) {
            return new ApiResponse<>("Không đủ thông tin để xóa!!!", null);
        }

        Optional<Service> service = serviceRepository.findById(serviceDTO.getId());

        if(service.isEmpty()) {
            return new ApiResponse<>("Không tìm thấy dịch vụ với thông tin đã cung cấp!!!", null);
        }

        serviceRepository.delete(service.get());

        return new ApiResponse<>("Đã xóa dịch vụ thành công", null);
    }

    @Override
    public PagedResponse<CategoryWithServiceDTO> getAllCategoriesWithServicesByStore(StoreOwner storeOwner, int page, int size) {
        LaundryShop shop = laundryShopRepository.findByStoreOwner(storeOwner);
        if (shop == null) {
            return new PagedResponse<>("Không tìm thấy cửa hàng", null);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<ServiceCategory> categoryPage = serviceCategoryRepository.findServiceCategoryByShop(shop, pageable);

        // Chuyển đổi dữ liệu thành danh sách CategoryWithServiceDTO
        List<CategoryWithServiceDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(category -> {
                    List<ServiceDTO> services = serviceRepository.findByCategory(category).stream()
                            .map(service -> new ServiceDTO(
                                    service.getId(),
                                    service.getName(),
                                    service.getDescription(),
                                    service.getEstimatedTime(),
                                    service.getImageDesc(),
                                    service.getPrice(),
                                    service.getCategory().getId()
                            )).toList();

                    return new CategoryWithServiceDTO(
                            category.getId(),
                            category.getName(),
                            category.getImageDesc(),
                            category.getDescription(),
                            services
                    );
                }).toList();

        // Tạo Meta object cho phân trang
        Meta<CategoryWithServiceDTO> meta = new Meta<>(
                page,
                size,
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages()
        );

        // Tạo PagedData
        PagedData<CategoryWithServiceDTO> pagedData = new PagedData<>(meta, categoryDTOs);

        // Tạo PagedResponse
        return new PagedResponse<>("Lấy danh sách dịch vụ thành công", pagedData);
    }

    @Override
    public PagedResponse<OrderResponse> getAllOrdersByStoreOwner(StoreOwner storeOwner, int page, int size) {
        LaundryShop shop = laundryShopRepository.findByStoreOwner(storeOwner);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Order> orderPage = orderRepository.findOrdersByLaundryShop(shop, pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(order -> {
                    OrderResponse response = new OrderResponse();
                    response.setId(order.getId());
                    response.setUsername(order.getCustomer().getUsername());
                    response.setPhone(order.getCustomer().getPhone());
                    response.setEmail(order.getCustomer().getEmail());
                    response.setAddress(order.getCustomer().getAddress());
                    response.setTotalAmount(order.getTotalAmount());
                    response.setOrderStatus(order.getOrderStatus());
                    response.setImgProduct(order.getImgProduct());
                    response.setLaundryShopName(shop.getName());
                    response.setServiceCategoryName(order.getService().getCategory().getName());
                    response.setServiceName(order.getService().getName());
                    response.setServicePrice(order.getService().getPrice());
                    response.setOrderVolume(order.getOrderVolume());
                    response.setCreatedAt(order.getCreatedAt());
                    response.setInstructions(order.getInstructions());
                    response.setStatus(order.getOrderStatus());

                    return response;
                })
                .toList();

        Meta meta = new Meta(
                orderPage.getNumber() + 1,
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );
        PagedData<OrderResponse> pagedData = new PagedData<>(meta, orderResponses);
        PagedResponse<OrderResponse> response = new PagedResponse<>("Lấy danh sách thành công", pagedData);

        return ResponseEntity.ok(response).getBody();
    }

    private Employee findEmployeeByInfo(EmployeeDTO employeeDTO) {
        Employee employee = null;

        if (employeeDTO.getUsername() != null && !employeeDTO.getUsername().isEmpty()) {
            employee = employeeRepository.findByUsername(employeeDTO.getUsername());
            if (employee != null) return employee;
        }

        if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().isEmpty()) {
            employee = employeeRepository.findByEmail(employeeDTO.getEmail());
            if (employee != null) return employee;
        }

        if (employeeDTO.getPhone() != null && !employeeDTO.getPhone().isEmpty()) {
            employee = employeeRepository.findByPhone(employeeDTO.getPhone());
          return employee;
        }

        return null;
    }

    private LaundryShopDTO convertToLaundryDTO(LaundryShop shop) {
        LaundryShopDTO dto = new LaundryShopDTO();
        dto.setId(shop.getId());
        dto.setName(shop.getName());
        dto.setAddress(shop.getAddress());
        dto.setOpeningHours(shop.getOpeningHours());
        dto.setDescription(shop.getDescription());
        dto.setAverageRating(shop.getAverageRating());
        dto.setCreatedAt(shop.getCreatedAt());

        LaundryShopDTO.StoreOwnerSimpleDTO ownerDTO = new LaundryShopDTO.StoreOwnerSimpleDTO();
        ownerDTO.setUsername(shop.getStoreOwner().getUsername());

        dto.setStoreOwner(ownerDTO);

        return dto;
    }

    private ServiceDTO convertToServiceDTO(Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setImageDesc(service.getImageDesc());
        dto.setEstimatedTime(service.getEstimatedTime());

        return dto;
    }

    private ServiceCategoryDTO convertToServiceCategoryDTO(ServiceCategory serviceCategory) {
        ServiceCategoryDTO dto = new ServiceCategoryDTO();
        dto.setId(serviceCategory.getId());
        dto.setName(serviceCategory.getName());
        dto.setDescription(serviceCategory.getDescription());
        dto.setImageDesc(serviceCategory.getImageDesc());

        return dto;
    }
}