package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutboundDetailDto {
    private int outbound_detail_id;
    private int outbound_id;
    private String product_name;
    private String save_name;
    private int quantity;
    private String approval_status;
}
