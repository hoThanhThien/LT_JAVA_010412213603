package com.example.laundry.services.impl;

import com.example.laundry.dto.*;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.user.Employee;
import com.example.laundry.models.user.StoreOwner;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Employee addEmployee(Employee employee) {
        employeeRepository.save(employee);
      return employee;
    }

    @Override
    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public ApiResponse<Order> updateOrderStatus(Integer orderId, OrderStatus status, Employee employee) {
    // Kiểm tra đơn hàng tồn tại
    Optional<Order> orderOptional = orderRepository.findById(orderId);
    if (orderOptional.isEmpty()) {
      return new ApiResponse<>("Không tìm thấy đơn hàng với ID: " + orderId);
    }

    Order order = orderOptional.get();

    if(!order.getLaundryShop().getId().equals(employee.getShop().getId())) {
      return new ApiResponse<>("Nhân viên này không có quyền!!!");
    }

    // Cập nhật trạng thái
    order.setOrderStatus(status);
    order.setUpdatedAt(new Date());

    orderRepository.save(order);

    String message = "Đơn hàng #" + orderId + " đã được cập nhật thành " + status.name();

    return new ApiResponse<>(message);
  }

    @Override
    public PagedResponse<OrderResponse> getOrders(Employee employee, int page, int size) {
      LaundryShop laundryShop = employee.getShop();

      Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

      Page<Order> orderPage = orderRepository.findOrdersByLaundryShop(laundryShop, pageable);

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
                response.setLaundryShopName(order.getLaundryShop().getName());
                response.setServiceCategoryName(order.getServiceCategory().getName());
                response.setServiceName(order.getService().getName());
                response.setServicePrice(order.getService().getPrice());
                response.setOrderVolume(order.getOrderVolume());
                response.setCreatedAt(order.getCreatedAt());
                response.setInstructions(order.getInstructions());

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

    @Override
    public PagedResponse<EmployeeDTO> getAllEmployees(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Employee> employeePage = employeeRepository.findAll(pageable);

    List<EmployeeDTO> dtos = employeePage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());

    Meta meta = new Meta(
            employeePage.getNumber() + 1,
            employeePage.getSize(),
            employeePage.getTotalElements(),
            employeePage.getTotalPages()
    );

    PagedData<EmployeeDTO> pagedData = new PagedData<>(meta, dtos);
    return new PagedResponse<>("Lấy danh sách nhân viên thành công", pagedData);
  }

    @Override
    public PagedResponse<EmployeeDTO> getAllEmployeesByStoreOwner(StoreOwner storeOwner, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Employee> employeePage = employeeRepository.findByStoreOwner(storeOwner, pageable);

    List<EmployeeDTO> dtos = employeePage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());

    Meta meta = new Meta(
            employeePage.getNumber() + 1,
            employeePage.getSize(),
            employeePage.getTotalElements(),
            employeePage.getTotalPages()
    );

    PagedData<EmployeeDTO> pagedData = new PagedData<>(meta, dtos);
    return new PagedResponse<>("Lấy danh sách nhân viên thành công", pagedData);
  }

    private EmployeeDTO convertToDTO(Employee employee) {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setUsername(employee.getUsername());
    dto.setEmail(employee.getEmail());
    dto.setPhone(employee.getPhone());
    dto.setAddress(employee.getAddress());
    dto.setRole(employee.getRoles());
    dto.setAvtUser(employee.getAvtUser());
    dto.setCreatedAt(employee.getCreatedAt());
    dto.setUpdatedAt(employee.getUpdatedAt());
    dto.setShopName(employee.getShop().getName());

    return dto;
  }

}
