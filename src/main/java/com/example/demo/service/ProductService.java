package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductListDto;
import com.example.demo.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;

    // 상품목록
    public List<ProductListDto> getAllProducts() {
        return productMapper.selectProductList();
    }
    public ProductDto getProductById(Long id) {
        return productMapper.selectProductById(id);
    }
}