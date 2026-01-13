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

    public List<Inbound_detailDto> inboundAppList(int inbound_id){
        return mapper.inboundAppList(inbound_id);
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

    public List<Outbound_detailDto> outboundAppList(int outbound_id){
        return mapper.outboundAppList(outbound_id);
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
       3. 승인 / 반려 처리 (요청 단위)
       =============================== */

    // 입고 요청 승인
    public boolean approvalIn(int approver_id, int inbound_id, String approval_status){
        Map<String,Object> map = new HashMap<>();
        map.put("approver_id", approver_id);
        map.put("inbound_id", inbound_id);
        map.put("approval_status", approval_status);
        return mapper.approvalIn(map) > 0;
    }

    // 출고 요청 승인
    public boolean approvalOut(int approver_id, int outbound_id, String approval_status){
        Map<String,Object> map = new HashMap<>();
        map.put("approver_id", approver_id);
        map.put("outbound_id", outbound_id);
        map.put("approval_status", approval_status);
        return mapper.approvalOut(map) > 0;
    }

    // 입고 요청 반려
    public boolean rejectionIn(int approver_id, int inbound_id, String approval_status){
        Map<String,Object> map = new HashMap<>();
        map.put("approver_id", approver_id);
        map.put("inbound_id", inbound_id);
        map.put("approval_status", approval_status);
        return mapper.rejectionIn(map) > 0;
    }

    // 출고 요청 반려
    public boolean rejectionOut(int approver_id, int outbound_id, String approval_status){
        Map<String,Object> map = new HashMap<>();
        map.put("approver_id", approver_id);
        map.put("outbound_id", outbound_id);
        map.put("approval_status", approval_status);
        return mapper.rejectionOut(map) > 0;
    }

    /* ===============================
       4. 상세 승인 상태 변경 (상품 단위)
       =============================== */

    // 입고 상세 승인 상태 변경
    public boolean inboundDetailStatus(int inbound_detail_id, String approval_status, String reason){
        Map<String,Object> map = new HashMap<>();
        map.put("inbound_detail_id", inbound_detail_id);
        map.put("approval_status", approval_status);
        map.put("reason", reason);
        return mapper.inboundDetailStatus(map) > 0;
    }

    // 출고 상세 승인 상태 변경
    public boolean outboundDetailStatus(int outbound_detail_id, String approval_status, String reason){
        Map<String,Object> map = new HashMap<>();
        map.put("outbound_detail_id", outbound_detail_id);
        map.put("approval_status", approval_status);
        map.put("reason", reason);
        return mapper.outboundDetailStatus(map) > 0;
    }

    /* ===============================
       5. 입출고 테이블 승인 상태 변경
       =============================== */

    // 입고 승인 상태 변경
    public boolean inboundStatus(int inbound_id, String approval_status){
        Map<String,Object> map = new HashMap<>();
        map.put("inbound_id", inbound_id);
        map.put("approval_status", approval_status);
        return mapper.inboundStatus(map) > 0;
    }

    // 출고 승인 상태 변경
    public boolean outboundStatus(int outbound_id, String approval_status){
        Map<String,Object> map = new HashMap<>();
        map.put("outbound_id", outbound_id);
        map.put("approval_status", approval_status);
        return mapper.outboundStatus(map) > 0;
    }

    /* ===============================
       6. 승인 내역 조회
       =============================== */

    // 승인 내역 전체 조회
    public Map<String,Object> approvalAll(int pageNum){
        Map<String,Object> map=new HashMap<>();
        map.put("pageNum",pageNum);

        int totalRowCount=mapper.approvalCount();

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

        int totalRowCount=mapper.approvalListCount(boundType);

        PageInfo pageInfo=new PageInfo(pageNum,5,5,totalRowCount);

        map.put("startRow",pageInfo.getStartRow());
        map.put("endRow",pageInfo.getEndRow());

        List<ApprovalDto> list=mapper.approvalList(map);

        Map<String,Object> result=new HashMap<>();
        result.put("list",list);
        result.put("pageInfo",pageInfo);

        return result;
    }

    public List<ApprovalDto> approvalconfAll(){
        List<ApprovalDto> approvalDtos=mapper.approvalconfAll();
        List<ApprovalDto> result = new ArrayList<>();

        for(ApprovalDto a:approvalDtos){
            boolean hasRequest = false;
            if(a.getBound_type().equals("IN")){
                List<String> list=inbound_detailMapper.select_detail_status(a.getInbound_id());
                for(String s:list){
                    if ("REQUEST".equals(s)) {
                        hasRequest = true;
                        break;
                    }
                }
            }else{
                List<String> list=outbound_detailMapper.select_detail_status(a.getOutbound_id());
                for(String s:list){
                    if ("REQUEST".equals(s)) {
                        hasRequest = true;
                        break;
                    }
                }
            }
            if (hasRequest) {
                result.add(a);
            }
        }
        System.out.println(result);
        return result;
    }

    // 입고 번호로 승인 내역 조회
    public ApprovalDto selectApprovalIn(int inbound_id){
        return mapper.selectApprovalIn(inbound_id);
    }

    // 출고 번호로 승인 내역 조회
    public ApprovalDto selectApprovalOut(int outbound_id){
        return mapper.selectApprovalOut(outbound_id);
    }

    /* ===============================
       7. 승인 상태 보정
       =============================== */

    // 승인된 입고 요청에 반려 상품이 추가되었을 때 상태 수정
    public boolean updateApprovalIn(int inbound_id){
        return mapper.updateApprovalIn(inbound_id) > 0;
    }

    // 승인된 출고 요청에 반려 상품이 추가되었을 때 상태 수정
    public boolean updateApprovalOut(int outbound_id){
        return mapper.updateApprovalOut(outbound_id) > 0;
    }

    /* ===============================
       8. 단건 조회
       =============================== */

    // 입고 단건 조회
    public InboundDto selectInboundId(int inbound_id){
        return mapper.selectInboundId(inbound_id);
    }

    // 출고 단건 조회
    public OutboundDto selectOutboundId(int outbound_id){
        return mapper.selectOutboundId(outbound_id);
    }
}
