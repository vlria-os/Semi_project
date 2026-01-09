package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data// getter, setter, toString, equals, hashCode 모두 생성
public class ProductStockDto {
    private int product_id;
    private String product_name;
    private Integer category_id;
    private Integer price;
    private String unit;
    private Integer image_id;
    private String status;
    private String category_name;
    private Long stock_id;
    private Long lot_in_id;
    private Integer quantity;
    private String category_path;
}
