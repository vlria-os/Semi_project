package com.example.demo.mapper;

import com.example.demo.dto.InboundDto;
import com.example.demo.dto.Inbound_detailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Inbound_detailMapper {
    int insert(Inbound_detailDto inbound_detailDto);
    int update_status(Inbound_detailDto inbound_detailDto);
    int update_inbStatus_rej(int inbound_detail_id);
    int update_inbStatus_conf(int inbound_detail_id);
    int update_appStatus(Inbound_detailDto inbound_detailDto);
    int update_appStatus_rej(int inbound_detail_id);
    int update_appStatus_conf(int inbound_detail_id);
    int update_reason(Inbound_detailDto inbound_detailDto);
    List<String> select_detail_status(int inbound_id);
}
