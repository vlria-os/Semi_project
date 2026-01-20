package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocalProductDto {
    private String areaNm;
    private String cntntsSj;
    private String imgUrl;
    private String linkUrl;
}
