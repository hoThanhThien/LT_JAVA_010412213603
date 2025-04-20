package com.example.laundry.services.impl;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.user.Employee;
import com.example.laundry.repository.EmployeeRepository;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.services.EmployeeService;
import com.example.laundry.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
  @Autowired
  private OrderRepository orderRepository;

    @Override
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
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

//    @Override
//    public void notifyOrderCompleted(Employee employee, Long orderId) {
//        employeeRepository.notifyOrderCompleted(employee, orderId);
//    }
//
//    @Override
//    public void notifyCustomer(Employee employee, Long orderId, String message, String notificationType) {
//        employeeRepository.notifyCustomer(employee, orderId, message, notificationType);
//    }
}
