package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebuserDTO {
    private int webuser_id;
    private String id;
    private String password;
    private String webuser_name;
    private int role_id;
    private String is_active;
    private LocalDate created_at;
}
