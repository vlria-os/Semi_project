package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.Product_imageDto;
import com.example.demo.dto.SearchDto;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;
    private final String UPLOADPATH = "c:/image_Semi/";

    public Map<String,Object> productList(int pageNum, SearchDto searchDto){
        Map<String,Object> map=new HashMap<>();
        map.put("searchDto", searchDto);

        int totalRowCount=productMapper.count(map);
        PageInfo pageInfo=new PageInfo(pageNum,3,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<ProductDto> productDtos=productMapper.selectAllKeyword(map);

        Map<String,Object> result=new HashMap<>();
        result.put("productDtos",productDtos);
        result.put("pageInfo",pageInfo);
        return result;
    }

    @Transactional
    public int updateProduct(ProductDto productDto){
        MultipartFile file= productDto.getFile();

        if(file != null && !file.isEmpty()){
            String orgFileName= file.getOriginalFilename();
            String extName= orgFileName.substring(orgFileName.lastIndexOf("."));
            String saveFileName= UUID.randomUUID() + extName;

            try{
                file.transferTo(new File(UPLOADPATH + saveFileName));
                Product_imageDto imageDto= new Product_imageDto(0, UPLOADPATH + saveFileName, saveFileName);
                productMapper.insert_image(imageDto);

                productDto.setImage_id(imageDto.getImage_id());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return productMapper.updateProduct(productDto);
    }

    public ProductDto getProductById(int productId){
        ProductDto dto= productMapper.selectOneProduct(productId);
        if(dto != null){
            dto.setSave_name(imageMapper.select(dto.getProduct_id()));
        }
        return dto;
    }

    @Transactional
    public void deleteProduct(int productId){
        String saveName=productMapper.select_product(productId);
        File file= new File(UPLOADPATH + saveName);
        if(file.exists()) file.delete(); //파일삭제

        productMapper.deleteProduct(productId);
    }

//    public Map<String,Object> productList(int pageNum, String keyword){
//        Map<String,Object> map=new HashMap<>();
//        map.put("pageNum",pageNum);
//
//        int totalRowCount=productMapper.count(keyword);
//
//        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);
//
//        map.put("startRow",pageInfo.getStartRow());
//        map.put("endRow",pageInfo.getEndRow());
//        map.put("keyword",keyword);
//
//        List<ProductDto> productDtos=productMapper.selectAll_keyword(map);
//
//        for(ProductDto p:productDtos){
//            String category=p.getCategory();
//            p.setCategory(category);
//            p.setSave_name(imageMapper.select(p.getProduct_id()));
//        }
//
//        Map<String,Object> result=new HashMap<>();
//        result.put("productDtos",productDtos);
//        result.put("pageInfo",pageInfo);
//        return result;
//    }
     @Transactional
    public int insert(ProductDto productDto,
                      Product_imageDto product_imageDto){
//        String category=productDto.getCategory();
//        productDto.setCategory_id(productMapper.selectOne_category(category));
        productMapper.insert_image(product_imageDto);
        productDto.setImage_id(product_imageDto.getImage_id());
        productMapper.insert(productDto);

        return 1;
    }

    public List<CategoryDto> getRootCategories(){
        return productMapper.selectRootCategories();
    }

    public List<CategoryDto> getChildrenByParentId(int parentId){
        return productMapper.selectChildrenByParentId(parentId);
    }

    public String getProductName(int productId){
        return productMapper.selectProductName(productId);
    }

}
