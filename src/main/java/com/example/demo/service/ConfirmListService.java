package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.ConfirmMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfirmListService {
    private final ConfirmMapper confirmMapper;

    public List<ConfirmInboundDto> Confirm_in_List(int pageNum){
        Map<String,Object> map=new HashMap<>();

        int totalRowCount=confirmMapper.count_in();
        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        return confirmMapper.select_confirm_in(map);
    }

    public List<ConfirmInDetailDto> Confirm_in_detailList(int inbound_id){
        return confirmMapper.select_confirm_detailIn(inbound_id);
    }

    public List<ConfirmOutboundDto> Confirm_out_List(int pageNum){
        Map<String,Object> map=new HashMap<>();

        int totalRowCount=confirmMapper.count_out();
        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        return confirmMapper.select_confirm_out(map);
    }

    public List<ConfirmOutDetailDto> Confirm_out_detailList(int outbound_id){
        return confirmMapper.select_confirm_detailOut(outbound_id);
    }
}
