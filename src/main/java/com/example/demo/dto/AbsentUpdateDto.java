package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsentUpdateDto {
    private String attendStatus;   // "WORK", "ANNUAL" ...
    private String checkInTime;    // "HH:mm" or null
    private String checkOutTime;   // "HH:mm" or null
}
