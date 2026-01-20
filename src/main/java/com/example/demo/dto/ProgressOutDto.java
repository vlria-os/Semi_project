package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressOutDto {
    private int outbound_id;
    private int outbound_detail_id;
    private String product_name;
    private int quantity;
    private String is_expired;
    private String warehouse_name;
    private String webuser_name;
    private String approver_name;
    private String confirmer_name;
    private LocalDate request_date;
    private LocalDate created_at;
    private LocalDate received_date;
}
