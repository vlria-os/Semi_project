package com.example.demo.mapper;

import com.example.demo.dto.ProductStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductStockMapper {

    List<ProductStockDto> selectAll(Map<String,Object> map);

    int count(Map<String,Object> map);

    void updateQuantity(
            @Param("productId")Long productId,
            @Param("quantity")int quantity
    );
}

