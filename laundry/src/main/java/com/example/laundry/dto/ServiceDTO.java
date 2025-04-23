package com.example.laundry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
    private String estimatedTime;
    private byte[] imageDesc;
    private Double price;

    public ServiceDTO(Long id, String name, String estimatedTime, Double price) {
        this.id = id;
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.price = price;
    }
}
