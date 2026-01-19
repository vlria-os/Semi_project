package com.example.demo.mapper;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.Product_imageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    List<ProductDto> selectAll();
    List<ProductDto> selectAllKeyword(Map<String, Object> map);
    int updateProduct(ProductDto productDto);
    ProductDto selectOneProduct(int productid);
    int deleteProduct(int productId);
//    List<ProductDto> selectAll_keyword(Map<String,Object> map);
    int count(Map<String, Object> map);
//    List<CategoryDto> select_category(int category_id);
    int insert(ProductDto productDto);
//    int selectOne_category(String category_name);
    int insert_image(Product_imageDto product_imageDto);
    String select_product(int product_id);
    String selectProductName(@Param("productId") int productId);

    List<CategoryDto> selectRootCategories();
    List<CategoryDto> selectChildrenByParentId(@Param("parentId") int parentId);
}
