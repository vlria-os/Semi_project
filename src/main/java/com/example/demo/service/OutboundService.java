package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboundService {
    private final ApprovalMapper approvalMapper;
    private final StockMapper stockMapper;
    private final Lot_outMapper lot_outMapper;
    private final OutboundMapper outboundMapper;
    private final Outbound_detailMapper outbound_detailMapper;

    @Transactional
    public int insert_request(List<Outbound_reqDto> outbound_reqDtos,
                              int webuser_id){
        int n=0;
        int m=0;

        OutboundDto outboundDto = new OutboundDto(
                0, webuser_id, null, "REQUEST");
        n += outboundMapper.insert(outboundDto);

        for(Outbound_reqDto outbound_reqDto:outbound_reqDtos) {
            int product_id = outbound_reqDto.getProduct_id();
            int quantity = outbound_reqDto.getQuantity();

            Outbound_detailDto outbound_detailDto = new Outbound_detailDto(
                    0, outboundDto.getOutbound_id(), product_id,
                    null, null, null, quantity);

            m += outbound_detailMapper.insert(outbound_detailDto);

        }

        if(n==1&&m==outbound_reqDtos.size()){
            return 1;
        }else{
            return -1;
        }
    }

    @Transactional
    public int update_approval(int outbound_id,
                               Outbound_detailDto outbound_detailDto,
                               int approver_id){

        System.out.println(outbound_detailDto);

        int product_id=outbound_detailMapper.select_product(outbound_detailDto.getOutbound_detail_id());
        outbound_detailDto.setQuantity(outbound_detailMapper.select_quantity(outbound_detailDto.getOutbound_detail_id()));

        int n=outbound_detailMapper.update_appStatus(outbound_detailDto);
        int m=outboundMapper.update_status(outbound_id);
        String status=outboundMapper.select_status(outbound_id);

        if(!status.equals("REQUEST")){
            ApprovalDto approvalDto=new ApprovalDto(
                    0,approver_id,"OUT",0,outbound_id,
                    status,null);
            int a=approvalMapper.insert_outbound(approvalDto);
        }

        if(outbound_detailDto.getApproval_status().equals("REJECTED")){
            int c=outbound_detailMapper.update_reason(outbound_detailDto);
        }

        //재고 확인
        List<Select_outboundDto> select_outboundDtos=stockMapper.select_outbound(product_id);
        int totalStock = 0;
        for (Select_outboundDto s : select_outboundDtos) {
            totalStock += s.getQuantity();
        }
        if (totalStock < outbound_detailDto.getQuantity()) {
            return -1;
        }

        //출고 확인 예정 로직
        int sum=0;
        int target = outbound_detailDto.getQuantity();

        for (Select_outboundDto s : select_outboundDtos) {
            if (sum >= target) break;
            int remain = target - sum;
            int useQty = Math.min(s.getQuantity(), remain);
            lot_outMapper.insert(
                    new Lot_outDto(0, outbound_detailDto.getOutbound_detail_id(), s.getWarehouse_id(),
                            null, s.getLot_in_id(), useQty, s.getStock_id(), null));
            stockMapper.update_out(new StockDto(s.getStock_id(), s.getLot_in_id(), useQty, product_id));
            sum += useQty;
        }

        return 1;
    }

    @Transactional
    public int insert_confirm(int outbound_detail_id,
                              String status,
                              String reason,
                              int confirmer_id){

        List<Lot_outDto> lot_outDtos=lot_outMapper.select_lot(outbound_detail_id);

        if(status.equals("REJECTED")){
            int m=outbound_detailMapper.update_outStatus_rej(outbound_detail_id);
            for (Lot_outDto l : lot_outDtos) {
                int product_id=outbound_detailMapper.select_product(outbound_detail_id);
                System.out.println("---->"+l.getQuantity());
                stockMapper.update_in(new StockDto(l.getStock_id(),0,l.getQuantity(), product_id));
                int q=lot_outMapper.delete(l.getLot_out_id());
                Outbound_detailDto outbound_detailDto=new Outbound_detailDto();
                outbound_detailDto.setOutbound_detail_id(outbound_detail_id);
                outbound_detailDto.setReason(reason);
                int a=outbound_detailMapper.update_reason(outbound_detailDto);
            }
        } else {
            for (Lot_outDto l : lot_outDtos) {
                int b=outbound_detailMapper.update_outStatus_conf(outbound_detail_id);
                int n=lot_outMapper.insert_date(l.getLot_out_id());
            }
        }

        return 1;
    }
}
