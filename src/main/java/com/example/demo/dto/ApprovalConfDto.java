package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApprovalConfDto {
    private int approval_id;
    private String approver_name;
    private Integer inbound_id;
    private Integer outbound_id;
    private String product_name;
    private String approval_status;
    private String is_refund;
    private LocalDateTime created_at;
}
