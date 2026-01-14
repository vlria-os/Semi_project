package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfirmOutDetailDto {
    private int outbound_detail_id;
    private int lot_out_id;
    private String product_name;
    private String warehouse_name;
    private LocalDate shipped_date;
    private String outbound_status;
    private int quantity;
}
