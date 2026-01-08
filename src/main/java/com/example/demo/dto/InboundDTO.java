package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InboundDTO {
    private int inbound_id;
    private int webuser_id;
    private LocalDateTime request_date;
    private String approval_status;
    private String is_refund;
    private int lot_out_id;
}
