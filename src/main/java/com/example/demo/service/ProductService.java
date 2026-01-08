package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;

    public List<ProductDto> productList(){
        List<ProductDto> productDtos=productMapper.selectAll_keyword("");
        for(ProductDto p:productDtos){
            String category=p.getCategory();
            p.setCategory(category);
        }
        return productDtos;
    }

    public List<ProductDto> productList(String keyword){
        List<ProductDto> productDtos=productMapper.selectAll_keyword(keyword);

        for(ProductDto p:productDtos){
            String category=p.getCategory();
            p.setCategory(category);
        }
        return productDtos;
    }

    public int insert(ProductDto productDto){
        String category=productDto.getCategory();
        productDto.setCategory_id(productMapper.selectOne_category(category));
        productMapper.insert(productDto);
        return 1;
    }
}
