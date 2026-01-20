package com.example.demo.mapper;

import com.example.demo.dto.ProgressInDto;
import com.example.demo.dto.ProgressOutDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProgressMapper {
    List<ProgressInDto> select_in(Map<String,Object> map);
    List<ProgressInDto> select_inMy(Map<String,Object> map);
    List<ProgressOutDto> select_out(Map<String,Object> map);
    List<ProgressOutDto> select_outMy(Map<String,Object> map);
    int count_in(String keyword);
    int count_inMy(Map<String,Object> map);
    int count_out(String keyword);
    int count_outMy(Map<String,Object> map);
}
