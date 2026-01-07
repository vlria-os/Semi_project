package com.example.demo.service;

import com.example.demo.dto.DueDateDto;
import com.example.demo.dto.WarehouseDto;
import com.example.demo.mapper.DueDateMapper;
import com.example.demo.mapper.WarehouseMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DueDateService {
    private final DueDateMapper dMapper;
    private final WarehouseMapper wMapper;

    public Map<String, Object> getDueDateList(int pageNum ,String typeFilter, String warehouseFilter, String viewType){
        Map<String, Object> map=new HashMap<>();
        map.put("typeFilter", typeFilter);
        map.put("warehouseFilter", warehouseFilter);
        int totalRowCount=dMapper.getDateCount(map);
        PageInfo pageInfo=new PageInfo(pageNum, 5, 5, totalRowCount);
        map.put("startRow", pageInfo.getStartRow());
        map.put("endRow", pageInfo.getEndRow());

        List<DueDateDto> list = dMapper.getDueDateList(map);

        Map<String, Object> result=new HashMap<>();
        result.put("list", list);
        result.put("pageInfo", pageInfo);
        return result;
    }

    public List<WarehouseDto> getWarehouse(){
        return wMapper.selectWarehouse();
    }

    public List<DueDateDto> getSideDateList(){
        Map<String, Object> map=new HashMap<>();
        map.put("startRow", 1);
        map.put("endRow", 3);
        return dMapper.getDueDateList(map);
    }
}
