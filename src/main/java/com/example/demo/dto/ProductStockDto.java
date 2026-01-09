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
    private Integer price;
    private String unit;

    private String status;

    private Long stock_id;
    private Long lot_in_id;
    private Integer quantity;
    private Integer category_id;
    private String category_name;
    private String category_path;
    private String image_path;
    private Integer image_id;

}
