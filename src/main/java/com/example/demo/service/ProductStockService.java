package com.example.demo.service;

import com.example.demo.dto.ProductStatus;
import com.example.demo.dto.ProductStockDto;
import com.example.demo.mapper.ProductStockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductStockService {
    private final ProductStockMapper mapper;
    private final ProductStockMapper stockMapper;

    //페이지별 데잍 가져오기
    public List<ProductStockDto> selectAll() {
        return mapper.selectAll();
    }

    //전체 데이터 수 조회(페이징용)
    public int count(String keyword,String status){
        return mapper.count(keyword,status);
        }
    public void updateQuantity(Long productId, int quantity){
        stockMapper.updateQuantity(productId,quantity);
    }
}


