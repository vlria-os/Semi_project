package com.example.demo.service;

import com.example.demo.dto.ProductStatus;
import com.example.demo.dto.ProductStockDto;
import com.example.demo.dto.StockQuantityDto;
import com.example.demo.mapper.ProductStockMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductStockService {
    private final ProductStockMapper stockMapper;

    //페이지별 데잍 가져오기
    public Map<String,Object> selectAll(int pageNum,String keyword, String status) {
        Map<String,Object> map=new HashMap<>();
        map.put("keyword",keyword);
        map.put("status",status);

        int totalRowCount=stockMapper.count(map);

        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<ProductStockDto> list=stockMapper.selectAll(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    //전체 데이터 수 조회(페이징용)
    public int count(String keyword,String status){
            Map<String,Object> map=new HashMap<>();
            map.put("keyword",keyword);
            map.put("status",status);
            return stockMapper.count(map);
        }
    public void updateQuantity(Long productId, int quantity){
        int stock_id=stockMapper.select_stock(productId);
        stockMapper.updateQuantity(stock_id,quantity);
    }

    public List<StockQuantityDto> stockQuantityList(int productId){
        return stockMapper.stockQuantityList(productId);
    }

    public boolean stockQuantityUpdate(int quantity, int stockId, int lotInId){
        Map<String,Object> map=new HashMap<>();
        map.put("quantity",quantity);
        map.put("stockId",stockId);
        map.put("lotInId",lotInId);
        return stockMapper.updateQuantity(map) > 0;
    }
}


