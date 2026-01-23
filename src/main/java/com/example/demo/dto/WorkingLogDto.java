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
    private Integer workingLogId;
    private LocalDate workingDate;
    private Integer webuserId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String attendStatus;
    private String memo;
    private Integer canCheckoutLate;
}
