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


    public List<ListDto> selectlist() {
        return mapper.selectlist();

    }

    public List<ProductStockDto> selectAll(String product_name,Integer category_id,String category_name,String product_type) {
        return mapper.selectAll( product_name, category_id,category_name,product_type);
    }

}

