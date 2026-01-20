package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InboundDetailDto {
    private int inbound_detail_id;
    private int inbound_id;
    private String product_name;
    private String save_name;
    private int quantity;
    private String warehouse_name;
    private String approval_status;
}
