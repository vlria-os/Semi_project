package com.example.demo.mapper;

import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.SettlementDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {
    int select_IN_company(int product_id);
    int select_OUT_company(int product_id);
    int is_exist(int company_id);
    int insert_settlement(SettlementDto settlementDto);
    int Minus_pay(SettlementDto settlementDto);
    int Plus_pay(SettlementDto settlementDto);
    List<PaymentDto> select_list();
    PaymentDto select_list_one(int settlement_id);
    int update(SettlementDto settlementDto);
}
