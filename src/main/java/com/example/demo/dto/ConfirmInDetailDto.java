package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfirmInDetailDto {
    private int inbound_detail_id;
    private String product_name;
    private String warehouse_name;
    private LocalDate received_date;
    private String inbound_status;
    private LocalDate expiration_date;
    private int quantity;
}
