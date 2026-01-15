package com.example.demo.mapper;

import com.example.demo.dto.ProductStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductStockMapper {

    List<ProductStockDto> selectAll();

    int count(
            @Param("keyword")String keyword,
            @Param("status")String status
    );

    void updateQuantity(
            @Param("productId")Long productId,
            @Param("quantity")int quantity
    );
}

