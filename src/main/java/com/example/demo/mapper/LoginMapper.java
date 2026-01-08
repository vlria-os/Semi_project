package com.example.demo.mapper;

import com.example.demo.dto.WebuserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    WebuserDTO selectWebuser(String id);
}
