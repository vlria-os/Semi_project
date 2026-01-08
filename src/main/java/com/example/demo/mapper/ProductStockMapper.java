package com.example.demo.mapper;

import com.example.demo.dto.ListDto;
import com.example.demo.dto.ProductStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductStockMapper {
    List<ListDto> selectlist();

    List<ProductStockDto> selectAll(
            @Param("product_name")String product_name,
            @Param("category_id")Integer category_id,
            @Param("category_name")String category_name,
            @Param("product_type")String product_type
            );

    List<ProductStockDto> selectAll(String productName, Integer categoryId);
}

