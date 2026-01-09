package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDto {
    private Long productId;
    private String productName;
    private Long categoryId;
    private int price;
    private String unit;
    private Long imageId;
    private String productType;

    private String categoryName;
    private String filePath;
    private String originalName;

}
