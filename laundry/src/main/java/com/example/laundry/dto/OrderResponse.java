package com.example.laundry.dto;

import com.example.laundry.models.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
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
}
