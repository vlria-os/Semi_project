package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundDetail_OfficeDto {
    private int webuser_id;
    private int inbound_detail_id;
    private String product_name;
    private String warehouse_name;
    private String approval_status;
    private String inbound_status;
    private String reason;
    private int quantity;
}
