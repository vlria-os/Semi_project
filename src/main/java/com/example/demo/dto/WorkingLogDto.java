package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkingLogDto {
    private Integer working_log_id;
    private LocalDate working_date;
    private int webuser_id;
    private String name;
    private int role_id;
    private LocalDateTime check_in_time;
    private LocalDateTime check_out_time;
    private String attend_status;
    private String approval_status;
    private Integer approver_id;
    private LocalDateTime approval_date;
}
