package com.example.demo.mapper;

import com.example.demo.dto.DueDateDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DueDateMapper {
    List<DueDateDto> getDueDateList(Map<String, Object> map);
    int getDateCount(Map<String, Object> map);
}
