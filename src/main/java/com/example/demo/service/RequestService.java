package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.RequestMapper;
import com.example.demo.pagination.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestMapper mapper;

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

        List<BoundDTO> list=mapper.adminBound(map);

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

        List<BoundDTO> list=mapper.userBound(map);

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

        List<InboundDTO> list=mapper.inboundAll(map);

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

        List<InboundDTO> list=mapper.selectInbound(map);

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

        List<OutboundDTO> list=mapper.outboundAll(map);

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

        List<OutboundDTO> list=mapper.selectOutbound(map);

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
    public List<InboundDetailDTO> inboundList(int inbound_id){
        return mapper.inboundList(inbound_id);
    }

    // 출고 번호로 출고 상세 조회
    public List<OutboundDetailDTO> outboundList(int outbound_id){
        return mapper.outboundList(outbound_id);
    }

    // 입고 상세 번호로 상세 상품 조회
    public InboundDetailDTO selectDetailIn(int inbound_detail_id){
        return mapper.selectDetailIn(inbound_detail_id);
    }

    // 출고 상세 번호로 상세 상품 조회
    public OutboundDetailDTO selectDetailOut(int outbound_detail_id){
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
    public List<ApprovalDTO> approvalAll(){
        return mapper.approvalAll();
    }

    // 입고 번호로 승인 내역 조회
    public ApprovalDTO selectApprovalIn(int inbound_id){
        return mapper.selectApprovalIn(inbound_id);
    }

    // 출고 번호로 승인 내역 조회
    public ApprovalDTO selectApprovalOut(int outbound_id){
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
    public InboundDTO selectInboundId(int inbound_id){
        return mapper.selectInboundId(inbound_id);
    }

    // 출고 단건 조회
    public OutboundDTO selectOutboundId(int outbound_id){
        return mapper.selectOutboundId(outbound_id);
    }
}
