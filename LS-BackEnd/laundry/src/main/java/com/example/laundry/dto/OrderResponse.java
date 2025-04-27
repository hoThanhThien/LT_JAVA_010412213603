package com.example.laundry.dto;

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
}
