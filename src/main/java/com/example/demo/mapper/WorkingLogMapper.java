package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface WorkingLogMapper {
    int checkIn(Map<String,Object> map);
    int checkOut(@Param("webuserId") int webuserId);
}
