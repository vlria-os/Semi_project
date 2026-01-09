package com.example.demo.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Integer category_id;
    private String category_name;
    private Long parent_id;
}
