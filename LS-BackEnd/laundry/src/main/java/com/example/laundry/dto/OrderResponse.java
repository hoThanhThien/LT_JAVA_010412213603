package com.example.laundry.dto;

import com.example.laundry.models.order.Order;
import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.order.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String address;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private String imgProduct;
    private String laundryShopName;
    private String serviceCategoryName;
    private String serviceName;
    private Double servicePrice;
    private Double orderVolume;
    private Date createdAt;
    private String instructions;
    private OrderStatus status;
    private PaymentStatus paymentStatus;

    public OrderResponse(Long id, String username, OrderStatus orderStatus, Double totalAmount) {
        this.id = id;
        this.username = username;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
    }

    /**
     * Maps an Order entity into a standardized, null-safe API response DTO.
     */
    public static OrderResponse from(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        if (order.getCustomer() != null) {
            response.setUsername(order.getCustomer().getUsername());
            response.setPhone(order.getCustomer().getPhone());
            response.setEmail(order.getCustomer().getEmail());
            response.setAddress(order.getAddress() != null ? order.getAddress() : order.getCustomer().getAddress());
        } else {
            response.setUsername(order.getUsername());
            response.setAddress(order.getAddress());
        }
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderStatus(order.getOrderStatus());
        response.setStatus(order.getOrderStatus());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setImgProduct(order.getImgProduct());
        if (order.getLaundryShop() != null) {
            response.setLaundryShopName(order.getLaundryShop().getName());
        }
        if (order.getServiceCategory() != null) {
            response.setServiceCategoryName(order.getServiceCategory().getName());
        }
        if (order.getService() != null) {
            response.setServiceName(order.getService().getName());
            response.setServicePrice(order.getService().getPrice());
        }
        response.setOrderVolume(order.getOrderVolume());
        response.setCreatedAt(order.getCreatedAt());
        response.setInstructions(order.getInstructions());
        return response;
    }
}
