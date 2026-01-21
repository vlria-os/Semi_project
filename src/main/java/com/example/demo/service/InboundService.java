package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InboundService {
    private final WarehouseMapper warehouseMapper;
    private final InboundMapper inboundMapper;
    private final Inbound_detailMapper inbound_detailMapper;
    private final ApprovalMapper approvalMapper;
    private final Lot_inMapper lot_inMapper;
    private final StockMapper stockMapper;
    private final PaymentMapper paymentMapper;
    private final ProductMapper productMapper;

    @Transactional
    public int insert_request(List<Inbound_reqDto> inbound_reqDtos,
                              int webuser_id){
        int n=0;
        int m=0;

        InboundDto inboundDto = new InboundDto(
                0, webuser_id, null, null, "N", 0);
        n += inboundMapper.insert(inboundDto);

        for(Inbound_reqDto inbound_reqDto:inbound_reqDtos) {
            int product_id = inbound_reqDto.getProduct_id();
            int warehouse_id = inbound_reqDto.getWarehouse_id();
            int quantity = inbound_reqDto.getQuantity();

            Inbound_detailDto inbound_detailDto = new Inbound_detailDto(
                    0, inboundDto.getInbound_id(),product_id,warehouse_id,
                    null, null, null, quantity);

            m += inbound_detailMapper.insert(inbound_detailDto);

        }

        if(n==1&&m==inbound_reqDtos.size()){
            return 1;
        }else{
            return -1;
        }
    }

    @Transactional
    public int update_approval(int inbound_id,
                               Inbound_detailDto inbound_detailDto,
                               int approver_id){

        int warehouse_id=inbound_detailMapper.select_warehouse(inbound_detailDto.getInbound_detail_id());

        if(inbound_detailDto.getApproval_status().equals("REJECTED")){
            int a=inbound_detailMapper.update_appStatus_rej(inbound_detailDto.getInbound_detail_id());
            int c=inbound_detailMapper.update_reason(inbound_detailDto);
            int d=inboundMapper.update_status(inbound_id);
            return 1;
        }

        //창고 한계 수량 점검
        int stock=warehouseMapper.warehouse_stock(warehouse_id);
        int capacity=warehouseMapper.warehouse_capacity(warehouse_id);

        if(stock+inbound_detailDto.getQuantity()>capacity){
            return -1;
        }

        int n=inbound_detailMapper.update_appStatus(inbound_detailDto);
        int m=inboundMapper.update_status(inbound_id);
        String status=inboundMapper.select_status(inbound_id);

        if(!status.equals("REQUEST")){
            ApprovalDto approvalDto=new ApprovalDto(
                    0,approver_id,"IN",inbound_id,0,
                    status,null);
            int a=approvalMapper.insert_inbound(approvalDto);
        }

        return 1;
    }

    @Transactional
    public int insert_confirm(List<Lot_inDto> lot_inDtos,
                              String status,
                              String reason,
                              int confirmer_id){

        for (Lot_inDto l : lot_inDtos){
            l.setConfirmer_id(confirmer_id);
            l.setWarehouse_id(inbound_detailMapper.select_warehouse(l.getInbound_detail_id()));

            if(status.equals("REJECTED")){
                int m=inbound_detailMapper.update_inbStatus_rej(l.getInbound_detail_id());
                Inbound_detailDto inbound_detailDto=new Inbound_detailDto();
                inbound_detailDto.setInbound_detail_id(l.getInbound_detail_id());
                inbound_detailDto.setReason(reason);
                int a=inbound_detailMapper.update_reason(inbound_detailDto);
            }
            else {
                int n = lot_inMapper.insert(l);
                int q = stockMapper.insert(new StockDto(0, l.getLot_in_id(), l.getQuantity(), l.getProduct_id()));
                Inbound_detailDto inbound_detailDto=new Inbound_detailDto();
                inbound_detailDto.setInbound_detail_id(l.getInbound_detail_id());
                int a=inbound_detailMapper.update_inbStatus_conf(l.getInbound_detail_id());

                //정산
                int company_id=paymentMapper.select_IN_company(l.getProduct_id());
                int is_exist=paymentMapper.is_exist(company_id);
                int total_pay=(int)Math.ceil(l.getQuantity()*productMapper.select_price(l.getProduct_id())*0.8);
                if(is_exist==0){
                    int b=paymentMapper.insert_settlement(new SettlementDto(0,null,company_id,-(total_pay),"N",null,0));
                }else{
                    int c=paymentMapper.Minus_pay(new SettlementDto(0,null,is_exist,total_pay,"N",null,0));
                }
            }
        }

        return 1;
    }
}
