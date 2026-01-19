package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Ref_ExpService {
    private final OutboundMapper outboundMapper;
    private final InboundMapper inboundMapper;
    private final Outbound_detailMapper outbound_detailMapper;
    private final Inbound_detailMapper inbound_detailMapper;
    private final Lot_outMapper lot_outMapper;
    private final Lot_inMapper lot_inMapper;
    private final StockMapper stockMapper;

    @Transactional
    @Scheduled(cron = "0 20 10 * * *")
    public void insert_expiration(){
        List<Select_outboundDto> select_outboundDtos=stockMapper.select_expiration();

        OutboundDto outboundDto=new OutboundDto(0,0,null,"APPROVED");
        outboundMapper.insert_exp(outboundDto);

        for(Select_outboundDto s:select_outboundDtos){
            Outbound_detailDto outbound_detailDto=new Outbound_detailDto(0,outboundDto.getOutbound_id(), s.getProduct_id(),"APPROVED",null,"REQUEST",s.getQuantity());
            outbound_detailMapper.insert_exp(outbound_detailDto);

            lot_outMapper.insert_exp(new Lot_outDto(0,outbound_detailDto.getOutbound_detail_id(),s.getWarehouse_id(),"Y",s.getLot_in_id(),
                                s.getQuantity(),s.getStock_id(),null));
            stockMapper.update_out(new StockDto(s.getStock_id(),0, s.getQuantity(), s.getProduct_id()));
        }

        System.out.println("스케줄러 실행중...");
    }

    @Transactional
    public int insert_refund(int lot_out_id,
                              int webuser_id){
        int already_refunded=inboundMapper.is_refunded(lot_out_id);

        if(already_refunded==1){
            return -1;
        }

        InboundDto inboundDto=new InboundDto(0,webuser_id,null,null,"Y",lot_out_id);
        inboundMapper.insert(inboundDto);
        RefundDto refundDto=lot_outMapper.select_refund(lot_out_id);

        Inbound_detailDto inbound_detailDto=new Inbound_detailDto(0, inboundDto.getInbound_id(), refundDto.getProduct_id(),
                refundDto.getWarehouse_id(),null,null,null, refundDto.getQuantity());

        inbound_detailMapper.insert(inbound_detailDto);

        return 1;
    }

}
