package com.example.laundry.services.impl;

<<<<<<< HEAD
import com.example.laundry.dto.CreateOrderRequestDTO;
import com.example.laundry.dto.order.OrderResponseDTO;
import com.example.laundry.dto.UpdateOrderStatusRequestDTO;
import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.shop.LaundryShop;
// Đổi tên import nếu Service entity của bạn tên khác
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.user.Customer;
import com.example.laundry.repository.CustomerRepository;
import com.example.laundry.repository.LaundryShopRepository;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
// Đổi tên package Service nếu bạn đặt OrderServiceImpl vào package con
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
// Class này triển khai OrderService interface
public class OrderServiceImpl implements OrderService {

    // Các dependencies (Repositories) được inject vào implementation
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final LaundryShopRepository shopRepository;


    @Override // Đánh dấu phương thức này ghi đè từ interface
    public OrderResponseDTO createOrder(CreateOrderRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + requestDTO.getCustomerId()));

        LaundryShop shop = shopRepository.findById(requestDTO.getShopId())
                .orElseThrow(() -> new EntityNotFoundException("LaundryShop not found with id: " + requestDTO.getShopId()));

        // Đảm bảo tên lớp Service entity đúng
        com.example.laundry.models.shop.Service service = serviceRepository.findById(requestDTO.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + requestDTO.getServiceId()));

        // Giả định Service entity có phương thức getPrice() trả về BigDecimal
        BigDecimal totalAmount = service.getPrice();

        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setShop(shop);
        newOrder.setService(service); // Đảm bảo Order entity có setService()
        newOrder.setOrderStatus(OrderStatus.NEW);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setInstructions(requestDTO.getInstructions());
        // @CreationTimestamp trong Order entity sẽ tự xử lý createdAt
        // newOrder.setCreatedAt(new Date()); // Có thể bỏ dòng này nếu dùng @CreationTimestamp

        Order savedOrder = orderRepository.save(newOrder);
        return convertToDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = findOrderByIdOrThrow(orderId);
        return convertToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Customer not found with id: " + customerId);
        }
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO requestDTO) {
        Order order = findOrderByIdOrThrow(orderId);

        // Thêm logic kiểm tra chuyển trạng thái nếu cần
        // if (!isValidStatusTransition(order.getOrderStatus(), requestDTO.getNewStatus())) {
        //     throw new IllegalStateException("Invalid status transition");
        // }

        order.setOrderStatus(requestDTO.getNewStatus());
        // @UpdateTimestamp trong Order entity sẽ tự xử lý updatedAt
        // order.setUpdatedAt(new Date()); // Có thể bỏ dòng này nếu dùng @UpdateTimestamp

        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = findOrderByIdOrThrow(orderId);
        // Thêm logic kiểm tra quyền xóa hoặc trạng thái cho phép xóa nếu cần
        // if (order.getOrderStatus() != OrderStatus.NEW && order.getOrderStatus() != OrderStatus.CLOSED) {
        //    throw new IllegalStateException("Cannot delete order in status: " + order.getOrderStatus());
        // }
        orderRepository.delete(order); // Dùng delete(entity) hoặc deleteById(id)
    }


    // --- Private Helper Methods ---

    /**
     * Phương thức private để tìm Order hoặc ném Exception nếu không thấy
     */
    private Order findOrderByIdOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    /**
     * Chuyển đổi Order Entity sang OrderResponseDTO
     */
    private OrderResponseDTO convertToDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setInstructions(order.getInstructions());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            // Giả định Customer có phương thức getUsername()
            // dto.setCustomerUsername(order.getCustomer().getUsername());
        }
        if (order.getShop() != null) {
            dto.setShopId(order.getShop().getId());
            // Giả định LaundryShop có getName() và getAddress()
            // dto.setShopName(order.getShop().getName());
            // dto.setShopAddress(order.getShop().getAddress());
        }
        // Đảm bảo tên lớp Service entity và các phương thức getter đúng
        if (order.getService() != null) {
            dto.setServiceId(order.getService().getId());
            // Giả định Service entity có getName() và getPrice()
            // dto.setServiceName(order.getService().getName());
            // dto.setServicePrice(order.getService().getPrice());
        }
        return dto;
    }

    // Có thể thêm phương thức private kiểm tra logic chuyển trạng thái
    // private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) { ... }
}
=======
import com.example.laundry.models.order.Order;
import com.example.laundry.repository.OrderRepository;
import com.example.laundry.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

//    @Override
//    public void findById(int id) {
//        orderRepository.findById(id);
//    }
//
//    @Override
//    public void save(Order order) {
//        orderRepository.save(order);
//    }
//
//    @Override
//    public void deleteById(int id) {
//        orderRepository.deleteById(id);
//    }
//
//    @Override
//    public void updateOrderStatus(int orderId, String status) {
//        orderRepository.updateOrderStatus(orderId, status);
//    }
//
//    @Override
//    public void notifyCustomer(int orderId, String message) {
//        orderRepository.notifyCustomer(orderId, message);
//    }
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
