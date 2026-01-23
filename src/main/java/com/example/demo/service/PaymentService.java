package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.SettlementDto;
import com.example.demo.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentMapper paymentMapper;

    public List<PaymentDto> list(){
        return paymentMapper.select_list();
    }

    public PaymentDto list_one(int settlement_id){
        return paymentMapper.select_list_one(settlement_id);
    }

    public int update(int settlement_id, int webuser_id){
        SettlementDto settlementDto=new SettlementDto();
        settlementDto.setPayment_id(webuser_id);
        settlementDto.setSettlement_id(settlement_id);
        return paymentMapper.update(settlementDto);
    }

}
