package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.Product_imageDto;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;

    public Map<String,Object> productList(int pageNum){
        String keyword="";

        Map<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum);
        int totalRowCount=productMapper.count(keyword);

        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());
        map.put("keyword",keyword);

        List<ProductDto> productDtos=productMapper.selectAll_keyword(map);
        for(ProductDto p:productDtos){
            String category=p.getCategory();
            p.setCategory(category);
            p.setSave_name(imageMapper.select(p.getProduct_id()));
        }

        Map<String,Object> result=new HashMap<>();
        result.put("productDtos",productDtos);
        result.put("pageInfo",pageInfo);
        return result;
    }

    public Map<String,Object> productList(int pageNum, String keyword){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum);

        int totalRowCount=productMapper.count(keyword);

        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());
        map.put("keyword",keyword);

        List<ProductDto> productDtos=productMapper.selectAll_keyword(map);

        for(ProductDto p:productDtos){
            String category=p.getCategory();
            p.setCategory(category);
            p.setSave_name(imageMapper.select(p.getProduct_id()));
        }

        Map<String,Object> result=new HashMap<>();
        result.put("productDtos",productDtos);
        result.put("pageInfo",pageInfo);
        return result;
    }
     @Transactional
    public int insert(ProductDto productDto,
                      Product_imageDto product_imageDto){
        String category=productDto.getCategory();
        productDto.setCategory_id(productMapper.selectOne_category(category));
        productMapper.insert_image(product_imageDto);
        productDto.setImage_id(product_imageDto.getImage_id());
        productMapper.insert(productDto);

        return 1;
    }

}
