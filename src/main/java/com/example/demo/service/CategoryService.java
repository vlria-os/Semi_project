package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ProductMapper productMapper;
    public int insert(CategoryDto categoryDto){
        if(categoryDto.getParent_id()==0){
            return productMapper.insert_firstcategory(categoryDto);
        }
        return productMapper.insert_category(categoryDto);
    }
}
