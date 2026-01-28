package com.example.demo.service;

import com.example.demo.dto.ProgressInDto;
import com.example.demo.dto.ProgressOutDto;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ProgressMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressMapper progressMapper;

    public Map<String,Object> list(int pageNum, String keyword){
        Map<String,Object> map=new HashMap<>();

        int totalRowCount=progressMapper.count_in(keyword);
        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());
        map.put("keyword", keyword);

        Map<String,Object> result=new HashMap<>();
        result.put("list",progressMapper.select_in(map));
        System.out.println(progressMapper.select_in(map));
        result.put("pageInfo",pageInfo);
        return result;
    }

    public Map<String,Object> list_my(int pageNum, String keyword, int webuser_id){
        Map<String,Object> map=new HashMap<>();
        map.put("webuser_id", webuser_id);
        map.put("keyword", keyword);

        int totalRowCount=progressMapper.count_inMy(map);
        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());
        map.put("webuser_id", webuser_id);
        map.put("keyword", keyword);

        Map<String,Object> result=new HashMap<>();
        result.put("list",progressMapper.select_inMy(map));
        result.put("pageInfo",pageInfo);
        return result;
    }

    public Map<String,Object> list_out(int pageNum, String keyword){
        Map<String,Object> map=new HashMap<>();

        int totalRowCount=progressMapper.count_out(keyword);
        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());
        map.put("keyword", keyword);

        Map<String,Object> result=new HashMap<>();
        result.put("list",progressMapper.select_out(map));
        result.put("pageInfo",pageInfo);
        return result;
    }

    public Map<String,Object> list_outMy(int pageNum, String keyword, int webuser_id){
        Map<String,Object> map=new HashMap<>();
        map.put("webuser_id", webuser_id);
        map.put("keyword", keyword);

        int totalRowCount=progressMapper.count_outMy(map);
        PageInfo pageInfo=new PageInfo(pageNum,10,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        Map<String,Object> result=new HashMap<>();
        result.put("list",progressMapper.select_outMy(map));
        result.put("pageInfo",pageInfo);
        return result;
    }
}
