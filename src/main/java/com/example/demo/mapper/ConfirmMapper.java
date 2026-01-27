package com.example.demo.mapper;

import com.example.demo.dto.ConfirmInDetailDto;
import com.example.demo.dto.ConfirmInboundDto;
import com.example.demo.dto.ConfirmOutDetailDto;
import com.example.demo.dto.ConfirmOutboundDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConfirmMapper {
    List<ConfirmInboundDto> select_confirm_in(Map<String, Object> map);
    List<ConfirmInDetailDto> select_confirm_detailIn(int inbound_id);
    List<ConfirmOutboundDto> select_confirm_out(Map<String, Object> map);
    List<ConfirmOutDetailDto> select_confirm_detailOut(int inbound_id);
    int count_in();
    int count_out();
}
