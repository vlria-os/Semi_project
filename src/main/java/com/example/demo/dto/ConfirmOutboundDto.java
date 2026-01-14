package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfirmOutboundDto {
    private int outbound_id;
    private String is_expired;
    private LocalDate request_date;
    private String outbound_status;
}
