package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DueDateDto {
    private int stock_id;
    private int lot_in_id;
    private int lot_out_id;
    private String product_name;
    private String product_type;
    private String warehouse_name;
    private int warehouse_id;
    private Date expiration_date;
    private int quantity;
    private int days_left;
}
