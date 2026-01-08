package com.example.demo.mapper;

import com.example.demo.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RequestMapper {
    /* =========================
       1. 요청 내역 조회
       ========================= */

    /** 관리자 로그인 - 입고 요청 전체 조회 */
    List<InboundDTO> inboundAll(Map<String,Object> map);

    /** 관리자 로그인 - 입고 요청 페이징용 전체 입고 요청 건수 **/
    int adminInboundCount();

    /** 관리자 로그인 - 출고 요청 전체 조회 */
    List<OutboundDTO> outboundAll(Map<String,Object> map);

    /** 관리자 로그인 - 출고 요청 페이징용 전체 출고 요청 건수 **/
    int adminOutboundCount();

    /** 관리자 로그인 - 입출고 요청 전체 조회 */
    List<BoundDTO> adminBound(Map<String,Object> map);

    /** 관리자 로그인 - 입출고 요청 페이징용 전체 요청 건수 **/
    int adminCount();

    /** 직원 로그인 - 본인이 요청한 입고 내역 조회 */
    List<InboundDTO> selectInbound(Map<String,Object> map);

    /** 직원 로그인 - 입고 요청 건수 **/
    int userInboundCount(int webuser_id);

    /** 직원 로그인 - 본인이 요청한 출고 내역 조회 */
    List<OutboundDTO> selectOutbound(Map<String,Object> map);

    /** 직원 로그인 - 출고 요청 건수 **/
    int userOutboundCount(int webuser_id);

    /** 직원 로그인 - 본인이 요청한 입출고 전체 내역 조회 */
    List<BoundDTO> userBound(Map<String,Object> map);

    /** 직원 로그인 - 입출고 요청 건수 **/
    int userCount(int webuser_id);


    /* =========================
       2. 요청 상세 조회
       ========================= */

    /** 입고 번호로 입고 상세 내역 조회 */
    List<InboundDetailDTO> inboundList(int inbound_id);

    /** 출고 번호로 출고 상세 내역 조회 */
    List<OutboundDetailDTO> outboundList(int outbound_id);

    /** 입고 상세 번호로 단일 입고 상품 조회 */
    InboundDetailDTO selectDetailIn(int inbound_detail_id);

    /** 출고 상세 번호로 단일 출고 상품 조회 */
    OutboundDetailDTO selectDetailOut(int outbound_detail_id);


    /* =========================
       3. 승인 / 반려 처리 (승인 테이블 INSERT)
       ========================= */

    /** 관리자 입고 요청 승인 내역 추가 */
    int approvalIn(Map<String, Object> map);

    /** 관리자 입고 요청 반려 내역 추가 */
    int rejectionIn(Map<String, Object> map);

    /** 관리자 출고 요청 승인 내역 추가 */
    int approvalOut(Map<String, Object> map);

    /** 관리자 출고 요청 반려 내역 추가 */
    int rejectionOut(Map<String, Object> map);


    /* =========================
       4. 상태 변경 (UPDATE)
       ========================= */

    /** 입고 상세 테이블 승인 상태 및 반려 사유 수정 */
    int inboundDetailStatus(Map<String, Object> map);

    /** 출고 상세 테이블 승인 상태 및 반려 사유 수정 */
    int outboundDetailStatus(Map<String, Object> map);

    /** 입고 테이블 승인 상태 수정 */
    int inboundStatus(Map<String, Object> map);

    /** 출고 테이블 승인 상태 수정 */
    int outboundStatus(Map<String, Object> map);

    /** 승인된 입고 요청 건에 반려 상품 추가 시 승인 상태 수정 */
    int updateApprovalIn(int inbound_id);

    /** 승인된 출고 요청 건에 반려 상품 추가 시 승인 상태 수정 */
    int updateApprovalOut(int outbound_id);


    /* =========================
       5. 승인 내역 조회
       ========================= */

    /** 승인 내역 전체 조회 */
    List<ApprovalDTO> approvalAll();

    /** 입고 번호로 승인 내역 조회 */
    ApprovalDTO selectApprovalIn(int inbound_id);

    /** 출고 번호로 승인 내역 조회 */
    ApprovalDTO selectApprovalOut(int outbound_id);


    /* =========================
       6. 요청 단건 조회 (상태 확인용)
       ========================= */

    /** 입고 번호로 입고 요청 단건 조회 */
    InboundDTO selectInboundId(int inbound_id);

    /** 출고 번호로 출고 요청 단건 조회 */
    OutboundDTO selectOutboundId(int outbound_id);

}
