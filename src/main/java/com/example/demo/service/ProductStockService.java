package com.example.demo.service;

import com.example.demo.dto.ListDto;
import com.example.demo.dto.ProductStockDto;
import com.example.demo.mapper.ProductStockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductStockService {
    private final ProductStockMapper mapper;

    public List<ProductStockDto> selectAll(String keyword,String product_type) {
        return mapper.selectAll(keyword,product_type);
    }

}

