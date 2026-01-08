package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutboundDetailDTO {
    private int outbound_detail_id;
    private int outbound_id;
    private int product_id;
    private String approval_status;
    private String outbound_status;
    private int quantity;
    private String reason;
}
