package com.example.demo.mapper;

import com.example.demo.dto.Lot_inDto;
import com.example.demo.dto.Lot_outDto;
import com.example.demo.dto.RefundDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface Lot_outMapper {
    int insert(Lot_outDto lot_outDto);
    int insert_exp(Lot_outDto lot_outDto);
    int insert_date(int lot_out_id);
    int update_rej(Map<String,Object> map);
    List<Lot_outDto> select_lot(int outbound_detail_id);
    RefundDto select_refund(int lot_out_id);
    int insert_confirmer(Map<String,Object> map);
}
