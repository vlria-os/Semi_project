package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoundDTO {
    private String io_type;
    private String io_id;
    private String webuser_id;
    private LocalDateTime request_date;
    private String approval_status;
}
