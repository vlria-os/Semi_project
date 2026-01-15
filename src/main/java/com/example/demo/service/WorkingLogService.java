package com.example.demo.service;

import com.example.demo.dto.WebuserDto;
import com.example.demo.mapper.WebuserMapper;
import com.example.demo.mapper.WorkingLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkingLogService {
    private final WorkingLogMapper workingLogMapper;
    private final WebuserMapper webuserMapper;

    private boolean checkIn(int webuserId, int roleId){
        LocalTime checkInTime=LocalTime.now();
        WebuserDto dto=webuserMapper.selectWebuser(webuserId);
        Map<String,Object> map=new HashMap<>();
        map.put("webuserId",webuserId);
        map.put("name",dto.getWebuser_name());
        map.put("roleId",roleId);
        map.put("checkInTime",checkInTime);
        return workingLogMapper.checkIn(map) > 1;
    }
}
