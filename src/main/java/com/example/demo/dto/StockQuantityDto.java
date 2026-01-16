package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockQuantityDto {
    private Integer stockId;
    private Integer lotInId;
    private String productName;
    private Integer quantity;
    private LocalDate expirationDate;
}
