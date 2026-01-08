package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalesStatsDto {
    private long total_revenue;
    private long total_quantity;
    private long avg_price;
    private String product_name;
    private int quantity;
    private long revenue;
    private double share;
}
