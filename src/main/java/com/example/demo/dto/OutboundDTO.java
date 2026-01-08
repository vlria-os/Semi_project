package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutboundDTO {
    private int outbound_id;
    private int webuser_id;
    private LocalDateTime request_date;
    private String approval_status;
}
