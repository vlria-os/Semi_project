package com.example.demo.mapper;

import com.example.demo.dto.ProductStockDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductStockMapper {

    List<ProductStockDto> selectAll(
            @Param("keyword")String keyword,
            @Param("status")String status,
            @Param("offset") int offset,
            @Param("pagesize") int pagesize
            );

    int count(
            @Param("keyword")String keyword,
            @Param("status")String status
    );

    void updateQuantity(
            @Param("productId")Long productId,
            @Param("quantity")int quantity
    );
}

