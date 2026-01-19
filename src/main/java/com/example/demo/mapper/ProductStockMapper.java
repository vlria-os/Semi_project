package com.example.demo.mapper;

import com.example.demo.dto.ProductStockDto;
import com.example.demo.dto.StockQuantityDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductStockMapper {

    List<ProductStockDto> selectAll(Map<String,Object> map);

    int count(Map<String,Object> map);

    void updateQuantity(
            @Param("stock_id")int stock_id,
            @Param("quantity")int quantity
    );

    int select_stock(Long product_id);

    List<StockQuantityDto> stockQuantityList(@Param("productId") int productId);
    int updateQuantity(Map<String,Object> map);
}

