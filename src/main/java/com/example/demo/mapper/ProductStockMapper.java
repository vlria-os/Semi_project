package com.example.demo.mapper;

import com.example.demo.dto.ListDto;
import com.example.demo.dto.ProductStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductStockMapper {
    List<ProductStockDto> selectAll(String keyword, String status);
}

