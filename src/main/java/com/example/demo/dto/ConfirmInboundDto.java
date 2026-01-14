package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfirmInboundDto {
    private int inbound_id;
    private String is_refund;
    private LocalDate request_date;
    private String inbound_status;
}
