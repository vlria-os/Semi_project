package com.example.demo.mapper;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    // 전체조회
    List<ProductListDto> selectProductList();

    // 검색
    List<ProductListDto> searchProduct(@Param("keyword") String keyword);

    ProductDto selectProductById(Long id);

}