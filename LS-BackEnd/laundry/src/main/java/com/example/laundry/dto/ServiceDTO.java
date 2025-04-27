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
    private String imageDesc;
    private Double price;
    private Long categoryId;

    public ServiceDTO(Long id, String name, String description, String estimatedTime, String imageDesc, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.imageDesc = imageDesc;
        this.price = price;
    }

    public ServiceDTO(Long id, String name, String estimatedTime, Double price) {
        this.id = id;
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.price = price;
    }
}
