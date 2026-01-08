package com.example.demo.mapper;

import com.example.demo.dto.WebuserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WebuserMapper {
    List<WebuserDTO> webuserList(Map<String,Object> map);
    int count(Map<String,Object> map);
    WebuserDTO selectWebuser(int webuser_id);
    int updateWebuser(WebuserDTO dto);
    int insertWebuser(WebuserDTO dto);
    int deleteWebuser(int webuser_id);
    WebuserDTO idCheck(String id);
}
