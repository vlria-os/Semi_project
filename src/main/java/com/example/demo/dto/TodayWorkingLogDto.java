package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodayWorkingLogDto {
    private Integer workingLogId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}
