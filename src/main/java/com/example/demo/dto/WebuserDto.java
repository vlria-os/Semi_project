package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebuserDto {
    private Integer webuser_id;
    private String id;
    private String password;
    private String webuser_name;
    private Integer role_id;
    private String is_active;
    private LocalDateTime created_at;
}
