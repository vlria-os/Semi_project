package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.ConfirmMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfirmListService {
    private final ConfirmMapper confirmMapper;

    public List<ConfirmInboundDto> Confirm_in_List(){
        return confirmMapper.select_confirm_in();
    }

    public List<ConfirmInDetailDto> Confirm_in_detailList(int inbound_id){
        return confirmMapper.select_confirm_detailIn(inbound_id);
    }

    public List<ConfirmOutboundDto> Confirm_out_List(){
        return confirmMapper.select_confirm_out();
    }

    public List<ConfirmOutDetailDto> Confirm_out_detailList(int outbound_id){
        return confirmMapper.select_confirm_detailOut(outbound_id);
    }
}
