package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private int settlement_id;
    private LocalDate period;
    private int company_id;
    private int total_pay;
    private String payment_status;
    private LocalDate payment_date;
    private int payment_id;
    private String company_name;
    private String inout;
}
