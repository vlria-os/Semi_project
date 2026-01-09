package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Data  //무조건 넣어줌
public class ListDto {
    private int stock_id;
    private int lot_in_id;
    private int quantity;
    private int product_id;

}
