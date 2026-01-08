package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductListDto {
    private  Long productId;
    private  String productName;
    private  int price;
    private String productType;
    private String categoryName;
    private String filePath;

}
