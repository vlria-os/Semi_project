package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Outbound_detailDto {
    private Integer outbound_detail_id;
    private Integer outbound_id;
    private Integer product_id;
    private String approval_status;
    private String reason;
    private String outbound_status;
    private Integer quantity;
}
