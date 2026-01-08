package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private int product_id;
    private String product_name;
    private int category_id;
    private int price;
    private String unit;
    private int image_id;
    private String status;
    private String category;
}
