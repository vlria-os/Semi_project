package com.example.demo.mapper;

import com.example.demo.dto.WebuserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    WebuserDto selectWebuser(String id);
}
