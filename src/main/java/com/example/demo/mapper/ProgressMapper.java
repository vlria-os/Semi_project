package com.example.demo.mapper;

import com.example.demo.dto.ProgressInDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProgressMapper {
    List<ProgressInDto> select_in();
}
