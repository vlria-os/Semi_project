package com.example.demo.mapper;

import com.example.demo.dto.Outbound_detailDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Outbound_detailMapper {
    int insert(Outbound_detailDto outboundDetailDto);
    int insert_exp(Outbound_detailDto outboundDetailDto);
    int update_outStatus(Outbound_detailDto outbound_detailDto);
    int update_outStatus_rej(int outbound_detail_id);
    int update_outStatus_conf(int outbound_detail_id);
    int update_appStatus(Outbound_detailDto outbound_detailDto);
    int update_appStatus_rej(int outbound_detail_id);
    int update_appStatus_conf(int outbound_detail_id);
    int select_product(int outbound_detail);
    int update_reason(Outbound_detailDto outbound_detailDto);
    int select_quantity(int outbound_detail);
    List<String> select_detail_status(int outbound_id);
}
