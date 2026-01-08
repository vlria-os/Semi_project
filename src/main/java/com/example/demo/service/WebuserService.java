package com.example.demo.service;

import com.example.demo.dto.WebuserDTO;
import com.example.demo.mapper.WebuserMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebuserService {
    private final WebuserMapper mapper;

    public Map<String,Object> webuserList(int pageNum, String field,
                                        String keyword){
        Map<String,Object> map=new HashMap<>();
        map.put("field",field);
        map.put("keyword",keyword);

        //전체 사원 수
        int totalRowCount=mapper.count(map);

        PageInfo pageInfo=new PageInfo(pageNum,5,
                5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<WebuserDTO> list=mapper.webuserList(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    public WebuserDTO selectWebuser(int webuser_id){
        return mapper.selectWebuser(webuser_id);
    }
    public boolean insertWebuser(WebuserDTO dto){
        return mapper.insertWebuser(dto) > 0;
    }
    public boolean updateWebuser(WebuserDTO dto){
        return mapper.updateWebuser(dto) > 0;
    }
    public boolean deleteWebuser(int webuser_id){
        return mapper.deleteWebuser(webuser_id) > 0;
    }

    public WebuserDTO idCheck(String id){
        return mapper.idCheck(id);
    }
}
