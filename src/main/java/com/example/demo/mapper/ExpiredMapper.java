package com.example.demo.mapper;

import com.example.demo.dto.DueDateDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExpiredMapper {
    List<DueDateDto> getExpiredList(Map<String, Object> map);
    int getExpiredCount(Map<String, Object> map);
}
