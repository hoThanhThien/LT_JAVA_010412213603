package com.example.laundry.dto;

import com.example.laundry.models.order.OrderStatus;
import com.example.laundry.models.shop.LaundryShop;
import com.example.laundry.models.shop.Service;
import com.example.laundry.models.shop.ServiceCategory;
import com.example.laundry.models.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderDTO {
    private Double totalAmount;
    private OrderStatus orderStatus;
    private String imgProduct;
    private LaundryShop laundryShop;
    private ServiceCategory serviceCategory;
    private Service service;
    private Double orderVolume;
    private Date createdAt;
    private String instructions;

    public OrderDTO(Double totalAmount, OrderStatus orderStatus, String imgProduct,
                    Long laundryShopId, Long serviceCategoryId, Long serviceId,
                    Double orderVolume, Date createdAt, String instructions) {
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.imgProduct = imgProduct;
        this.laundryShop = new LaundryShop();
        this.laundryShop.setId(laundryShopId);
        this.serviceCategory = new ServiceCategory();
        this.serviceCategory.setId(serviceCategoryId);
        this.service = new Service();
        this.service.setId(serviceId);
        this.orderVolume = orderVolume;
        this.createdAt = createdAt;
        this.instructions = instructions;
    }
}
