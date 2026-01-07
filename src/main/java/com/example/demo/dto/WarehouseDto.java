package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseDto {
    private int warehouse_id;
    private String warehouse_name;
    private String warehouse_type;
    private String location;
    private long maximum_capacity;
    private String is_active;
    private Date created_at;
    private double usage_rate;
}
