package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutboundAllDto {
    private int outbound_id;
    private String product_name;
    private String webuser_name;
    private String approval_status;
    private LocalDateTime request_date;
}
