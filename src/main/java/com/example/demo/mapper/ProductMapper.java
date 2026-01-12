package com.example.demo.mapper;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.Product_imageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    List<ProductDto> selectAll();
    List<ProductDto> selectAll_keyword(Map<String,Object> map);
    List<CategoryDto> select_category(int category_id);
    int insert(ProductDto productDto);
    int selectOne_category(String category_name);
    int insert_image(Product_imageDto product_imageDto);
    String select_product(int product_id);
    int count(String keyword);
}
