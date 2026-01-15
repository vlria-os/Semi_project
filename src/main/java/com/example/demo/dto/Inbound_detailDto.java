package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inbound_detailDto {
    private Integer inbound_detail_id;
    private Integer inbound_id;
    private Integer product_id;
    private Integer warehouse_id;
    private String approval_status;
    private String reason;
    private String inbound_status;
    private Integer quantity;
}
