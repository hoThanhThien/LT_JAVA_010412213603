package com.example.laundry.controllers;

import com.example.laundry.models.order.Order;
import com.example.laundry.services.OrderService;
import com.example.laundry.dto.Meta;
import com.example.laundry.dto.PagingResponse;
import com.example.laundry.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import com.example.laundry.utils.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ApiResponse<PagingResponse<OrderDTO>> getOrders(
            @RequestParam(defaultValue = "1") int page,   // page tính từ 1
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size); // Spring paging bắt đầu từ 0
        Page<Order> orderPage = orderService.getAllOrders(pageable);

        List<OrderDTO> orderDTOList = orderPage.getContent()
                .stream()
                .map(this::convertToDTO) // dùng this:: cho gọn
                .collect(Collectors.toList());

        Meta meta = new Meta(page, size, orderPage.getTotalElements());
        PagingResponse<OrderDTO> pagingResponse = new PagingResponse<>(meta, orderDTOList);
        return new ApiResponse<>(pagingResponse, "Lấy thông tin thành công");
    }

    // Hàm convert Order -> OrderDTO
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        // Nếu Order có Customer
        if (order.getCustomer() != null) {
            dto.setCustomerName(order.getCustomer().getName());
        }
        dto.setStatus(order.getStatus());
        return dto;
    }
}

