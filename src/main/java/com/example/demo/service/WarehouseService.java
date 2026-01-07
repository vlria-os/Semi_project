package com.example.demo.service;

import com.example.demo.dto.SearchDto;
import com.example.demo.dto.WarehouseDto;
import com.example.demo.mapper.WarehouseMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseMapper mapper;

    public Map<String, Object> selectWarehouseList(int pageNum, SearchDto searchDto){
        Map<String, Object> map=new HashMap<>();
        map.put("field", searchDto.getField());
        map.put("keyword", (searchDto.getKeyword() != null) ? searchDto.getKeyword().trim() : "");
        map.put("search", searchDto.getSearch());

        int totalRowCount=mapper.getWarehouseCount(map);

        PageInfo pageInfo=new PageInfo(pageNum, 3, 3, totalRowCount);
        map.put("startRow", pageInfo.getStartRow());
        map.put("endRow", pageInfo.getEndRow());

        List<WarehouseDto> list=mapper.selectWarehouseList(map);

        Map<String, Object> result=new HashMap<>();
        result.put("list", list);
        result.put("pageInfo", pageInfo);
        return result;
    }

    public int insertWarehouse(WarehouseDto dto){return mapper.insertWarehouse(dto);}
    public void updateWarehouse(WarehouseDto dto){mapper.updateWarehouse(dto);}
    public int deleteWarehouse(int n){return mapper.deleteWarehouse(n);}
    public WarehouseDto selectAll(int n){return mapper.selectAll(n);}
    public List<WarehouseDto> selectWarehouse(){return mapper.selectWarehouse();}
    public List<WarehouseDto> getSideWarehouseList(){return mapper.selectLimit();}
}
