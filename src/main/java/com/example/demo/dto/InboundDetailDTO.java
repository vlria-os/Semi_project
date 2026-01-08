package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InboundDetailDTO {
    private int inbound_detail_id;
    private int inbound_id;
    private int product_id;
    private int warehouse_id;
    private String approval_status;
    private String inbound_status;
    private int quantity;
    private String reason;
}
