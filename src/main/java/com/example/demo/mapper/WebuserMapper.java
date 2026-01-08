package com.example.demo.mapper;

import com.example.demo.dto.WebuserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WebuserMapper {
    List<WebuserDto> webuserList(Map<String,Object> map);
    int count(Map<String,Object> map);
    WebuserDto selectWebuser(int webuser_id);
    int updateWebuser(WebuserDto dto);
    int insertWebuser(WebuserDto dto);
    int deleteWebuser(int webuser_id);
    WebuserDto idCheck(String id);
}
