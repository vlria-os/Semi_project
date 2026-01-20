package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.Inbound_detailMapper;
import com.example.demo.mapper.Outbound_detailMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.RequestMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestMapper mapper;
    private final ProductMapper productMapper;
    private final Inbound_detailMapper inbound_detailMapper;
    private final Outbound_detailMapper outbound_detailMapper;

   /* ===============================
       1. 입출고 요청 조회
       =============================== */

    // 관리자 로그인 입출고 요청 전체 내역
    public Map<String,Object> adminBound(int pageNum){
        Map<String,Object>map=new HashMap<>();
        map.put("pageNum",pageNum);

        int totalRowCount= mapper.adminCount();

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<BoundDto> list=mapper.adminBound(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    // 관리자 로그인 입출고 요청 페이징용 전체 요청 건수
    public int adminCount(){
        return mapper.adminCount();
    }

    // 사무직원 로그인 본인이 요청한 입출고 전체 내역
    public Map<String,Object> userBound(int pageNum, int webuser_id){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum);
        map.put("webuser_id",webuser_id);

        int totalRowCount = mapper.userCount(webuser_id);

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<BoundDto> list=mapper.userBound(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    // 직원 로그인 입출고 요청 페이징용 전체 요청 건수
    public int userCount(int webuser_id){
        return mapper.userCount(webuser_id);
    }

    // 관리자 로그인 입고 요청 내역
    public Map<String,Object> inboundAll(int pageNum){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum);

        int totalRowCount = mapper.adminInboundCount();

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<InboundDto> list=mapper.inboundAll(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    // 관리자 로그인 - 입고 요청 페이징용 요청 건수
    public int adminInboundCount(){
        return mapper.adminInboundCount();
    }

    // 사무직원 로그인 본인이 요청한 입고 요청
    public Map<String,Object> selectInbound(int pageNum, int webuser_id){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum);
        map.put("webuser_id",webuser_id);

        int totalRowCount=mapper.userInboundCount(webuser_id);

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<InboundDto> list=mapper.selectInbound(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    // 직원 로그인 입고 요청 페이징용 전체 요청 건수
    public int userInboundCount(int webuser_id){
        return mapper.userInboundCount(webuser_id);
    }

    // 관리자 로그인 출고 요청 내역
    public Map<String,Object> outboundAll(int pageNum){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum);

        int totalRowCount = mapper.adminOutboundCount();

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<OutboundDto> list=mapper.outboundAll(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    public List<InboundDetail_OfficeDto> requestInbound_detail_office(int inbound_id){
        return mapper.selectDetailInOffice(inbound_id);
    }

    public List<OutboundDetail_OfficeDto> requestOutbound_detail_office(int outbound_id){
        return mapper.selectDetailOutOffice(outbound_id);
    }

    // 관리자 로그인 - 출고 요청 페이징용 요청 건수
    public int adminOutboundCount(){
        return mapper.adminOutboundCount();
    }

    // 사무직원 로그인 본인이 요청한 출고 요청
    public Map<String,Object> selectOutbound(int pageNum, int webuser_id){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum);
        map.put("webuser_id",webuser_id);

        int totalRowCount=mapper.userOutboundCount(webuser_id);

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<OutboundDto> list=mapper.selectOutbound(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    // 직원 로그인 출고 요청 페이징용 전체 요청 건수
    public int userOutboundCount(int webuser_id){
        return mapper.userOutboundCount(webuser_id);
    }

    /* ===============================
       2. 입출고 상세 조회
       =============================== */

    // 입고 번호로 입고 상세 조회
    public Map<String,Object> inboundList(int inbound_id){
        List<Inbound_detailDto> list=mapper.inboundList(inbound_id);

        Map<String,Object> map=new HashMap<>();
        map.put("list",list);

        Map<Integer,String> names=new HashMap<>();
        for(Inbound_detailDto d:list){
            String name=mapper.getProductName(d.getProduct_id());
            names.put(d.getInbound_detail_id(),name);
        }

        map.put("names",names);

        return map;
    }

    public List<InboundDetailDto> inboundDetailList(int inbound_id){
        return mapper.inboundDetailList(inbound_id);
    }

    public Map<String,Object> inboundAppList(int inbound_id){
        List<Inbound_detailDto> list=mapper.inboundAppList(inbound_id);

        Map<Integer,Object> map=new HashMap<>();
        for(Inbound_detailDto i:list){
            map.put(i.getInbound_detail_id(),productMapper.selectProductName(i.getProduct_id()));
        }

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("names",map);
        return result;
    }

    public String product_name(int product_id){
        return productMapper.select_product(product_id);
    }

    // 출고 번호로 출고 상세 조회
    public Map<String,Object> outboundList(int outbound_id){
        List<Outbound_detailDto> list=mapper.outboundList(outbound_id);

        Map<String,Object> map=new HashMap<>();
        map.put("list",list);

        Map<Integer,String> names=new HashMap<>();
        for(Outbound_detailDto d:list){
            String name=mapper.getProductName(d.getProduct_id());
            names.put(d.getOutbound_detail_id(),name);
        }

        map.put("names",names);

        return map;
    }

    public List<OutboundDetailDto> outboundDetailList(int outbound_id){
        return mapper.outboundDetailList(outbound_id);
    }

    public Map<String,Object> outboundAppList(int outbound_id){
        List<Outbound_detailDto> list=mapper.outboundAppList(outbound_id);

        Map<Integer,Object> map=new HashMap<>();
        for(Outbound_detailDto o:list){
            map.put(o.getOutbound_detail_id(),productMapper.selectProductName(o.getProduct_id()));
        }

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("names",map);
        return result;
    }

    // 입고 상세 번호로 상세 상품 조회
    public Inbound_detailDto selectDetailIn(int inbound_detail_id){
        return mapper.selectDetailIn(inbound_detail_id);
    }

    // 출고 상세 번호로 상세 상품 조회
    public Outbound_detailDto selectDetailOut(int outbound_detail_id){
        return mapper.selectDetailOut(outbound_detail_id);
    }

    /* ===============================
       6. 승인 내역 조회
       =============================== */

    // 승인 내역 전체 조회
    public Map<String,Object> approvalAll(int pageNum, String field, String keyword){
        Map<String,Object> map=new HashMap<>();
        map.put("field",field);
        map.put("keyword",keyword);

        int totalRowCount=mapper.approvalCount(map);

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<ApprovalDto> list=mapper.approvalAll(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    //승인 내역 입고/출고 버튼별 조회
    public Map<String,Object> approvalList(int pageNum, String boundType){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("boundType",boundType);

        System.out.println(boundType);

        int totalRowCount=mapper.approvalListCount(boundType);

        System.out.println(totalRowCount);

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<ApprovalDto> list=mapper.approvalList(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

//    public List<ApprovalDto> approvalconfAll(){
//        List<ApprovalDto> approvalDtos=mapper.approvalconfAll();
//        List<ApprovalDto> result = new ArrayList<>();
//
//        for(ApprovalDto a:approvalDtos){
//            boolean hasRequest = false;
//            if(a.getBound_type().equals("IN")){
//                List<String> list=inbound_detailMapper.select_detail_status(a.getInbound_id());
//                for(String s:list){
//                    if ("REQUEST".equals(s)) {
//                        hasRequest = true;
//                        break;
//                    }
//                }
//            }else{
//                List<String> list=outbound_detailMapper.select_detail_status(a.getOutbound_id());
//                for(String s:list){
//                    if ("REQUEST".equals(s)) {
//                        hasRequest = true;
//                        break;
//                    }
//                }
//            }
//            if (hasRequest) {
//                result.add(a);
//            }
//        }
//        System.out.println(result);
//        return result;
//    }


    public List<ApprovalConfDto> getApprovalConfIn(){
        return mapper.approvalConfIn();
    }

    public List<ApprovalConfDto> getApprovalConfOut(){
        return mapper.approvalConfOut();
    }
}
