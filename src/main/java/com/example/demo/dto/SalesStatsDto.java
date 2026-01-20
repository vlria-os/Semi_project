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
    private long price;
    private int quantity;
    private long revenue;
    private double share;
    private String sale_date;
    private long waste_quantity;
    private double waste_rate;
}
