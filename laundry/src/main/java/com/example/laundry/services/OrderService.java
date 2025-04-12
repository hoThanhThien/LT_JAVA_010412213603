package com.example.laundry.services;

<<<<<<< HEAD
import com.example.laundry.dto.CreateOrderRequestDTO;
import com.example.laundry.dto.order.OrderResponseDTO;
import com.example.laundry.dto.UpdateOrderStatusRequestDTO;

import java.util.List;

public interface OrderService {

    /**
     * Tạo một đơn hàng mới
     * @param requestDTO Dữ liệu để tạo đơn hàng
     * @return DTO của đơn hàng đã tạo
     */
    OrderResponseDTO createOrder(CreateOrderRequestDTO requestDTO);

    /**
     * Lấy thông tin đơn hàng theo ID
     * @param orderId ID của đơn hàng cần lấy
     * @return DTO của đơn hàng
     */
    OrderResponseDTO getOrderById(Long orderId);

    /**
     * Lấy tất cả đơn hàng
     * @return Danh sách DTO của tất cả đơn hàng
     */
    List<OrderResponseDTO> getAllOrders();

    /**
     * Lấy các đơn hàng của một khách hàng cụ thể
     * @param customerId ID của khách hàng
     * @return Danh sách DTO các đơn hàng của khách hàng đó
     */
    List<OrderResponseDTO> getOrdersByCustomerId(Long customerId);

    /**
     * Cập nhật trạng thái đơn hàng
     * @param orderId ID của đơn hàng cần cập nhật
     * @param requestDTO Dữ liệu trạng thái mới
     * @return DTO của đơn hàng sau khi cập nhật
     */
    OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO requestDTO);

    /**
     * Xóa đơn hàng theo ID
     * @param orderId ID của đơn hàng cần xóa
     */
    void deleteOrder(Long orderId);
}
=======
import com.example.laundry.models.order.Order;

public interface OrderService {
//    void findById(int id);
//    void save(Order order);
//    void deleteById(int id);
//    void updateOrderStatus(int orderId, String status);
//    void notifyCustomer(int orderId, String message);
}
>>>>>>> 84721bd55a92f8a6da77804fa8a257fe7820d08a
