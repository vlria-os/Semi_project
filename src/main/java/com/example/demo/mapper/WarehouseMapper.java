package com.example.demo.mapper;

import com.example.demo.dto.WarehouseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarehouseMapper {
    int warehouse_stock(int warehouse_id);
    int warehouse_capacity(int warehouse_id);
    int getWarehouseCount(Map<String, Object> map);
    List<WarehouseDto> selectWarehouseList(Map<String, Object> map);
    int insertWarehouse(WarehouseDto dto);
    int updateWarehouse(WarehouseDto dto);
    int deleteWarehouse(int n);
    WarehouseDto selectAll(int n);
    List<WarehouseDto> selectWarehouse();
    List<WarehouseDto> getSideWarehouseList();
    List<WarehouseDto> warehouseType(String warehouse_type);
}
