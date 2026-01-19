package com.example.demo.service;

import com.example.demo.dto.DueDateDto;
import com.example.demo.dto.WarehouseDto;
import com.example.demo.mapper.ExpiredMapper;
import com.example.demo.mapper.WarehouseMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExpiredService {
    private final ExpiredMapper eMapper;
    private final WarehouseMapper wMapper;

    public List<WarehouseDto> getWarehouse(){
        return wMapper.selectWarehouse();
    }

    public Map<String, Object> getExpiredList(int pageNum, String typeFilter, String warehouseFilter, String keyword){
        Map<String, Object> map=new HashMap<>();
        map.put("typeFilter", typeFilter);
        map.put("warehouseFilter", warehouseFilter);
        map.put("keyword", keyword);

        int totalRowCount= eMapper.getExpiredCount(map);

        PageInfo pageInfo= new PageInfo(pageNum, 10, 5, totalRowCount);
        map.put("startRow", pageInfo.getStartRow());
        map.put("endRow", pageInfo.getEndRow());

        List<DueDateDto> list= eMapper.getExpiredList(map);
        Map<String, Object> result=new HashMap<>();
        result.put("list", list);
        result.put("pageInfo", pageInfo);
        return result;
    }
}
