package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lot_inDto {
    private int lot_in_id;
    private int inbound_detail_id;
    private int confirmer_id;
    private int warehouse_id;
    private String inbound_status;
    private LocalDate expiration_date;
    private LocalDate received_date;
    private int quantity;
    private int product_id;
}
