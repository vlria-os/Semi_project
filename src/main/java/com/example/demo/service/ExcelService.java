package com.example.demo.service;

import com.example.demo.dto.ApprovalDto;
import com.example.demo.dto.ProgressInDto;
import com.example.demo.dto.ProgressOutDto;
import com.example.demo.mapper.ApprovalMapper;
import com.example.demo.mapper.ProgressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final ApprovalMapper approvalMapper;
    private final ProgressMapper progressMapper;

    public List<ProgressInDto> download(){
        Map<String,Object> map=new HashMap<>();
        map.put("startRow",1);
        map.put("endRow",progressMapper.count_in(""));
        map.put("keyword", "");

        return progressMapper.select_in(map);
    }

    public List<ProgressOutDto> download_out(){
        Map<String,Object> map=new HashMap<>();
        map.put("startRow",1);
        map.put("endRow",progressMapper.count_out(""));
        map.put("keyword", "");

        return progressMapper.select_out(map);
    }
}
