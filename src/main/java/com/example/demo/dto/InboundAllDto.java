package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InboundAllDto {
    private int inbound_id;
    private String product_name;
    private String webuser_name;
    private String approval_status;
    private String is_refund;
    private LocalDateTime request_date;
}
