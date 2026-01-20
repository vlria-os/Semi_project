package com.example.demo.mapper;

import com.example.demo.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RequestMapper {
    /* =========================
       1. 요청 내역 조회
       ========================= */

    /** 관리자 로그인 - 입고 요청 전체 조회 */
    List<InboundDto> inboundAll(Map<String,Object> map);

    /** 관리자 로그인 - 입고 요청 페이징용 전체 입고 요청 건수 **/
    int adminInboundCount();

    /** 관리자 로그인 - 출고 요청 전체 조회 */
    List<OutboundDto> outboundAll(Map<String,Object> map);

    /** 관리자 로그인 - 출고 요청 페이징용 전체 출고 요청 건수 **/
    int adminOutboundCount();

    /** 관리자 로그인 - 입출고 요청 전체 조회 */
    List<BoundDto> adminBound(Map<String,Object> map);

    /** 관리자 로그인 - 입출고 요청 페이징용 전체 요청 건수 **/
    int adminCount();

    /** 직원 로그인 - 본인이 요청한 입고 내역 조회 */
    List<InboundDto> selectInbound(Map<String,Object> map);

    /** 직원 로그인 - 입고 요청 건수 **/
    int userInboundCount(int webuser_id);

    /** 직원 로그인 - 본인이 요청한 출고 내역 조회 */
    List<OutboundDto> selectOutbound(Map<String,Object> map);

    /** 직원 로그인 - 출고 요청 건수 **/
    int userOutboundCount(int webuser_id);

    /** 직원 로그인 - 본인이 요청한 입출고 전체 내역 조회 */
    List<BoundDto> userBound(Map<String,Object> map);

    /** 직원 로그인 - 입출고 요청 건수 **/
    int userCount(int webuser_id);


    /* =========================
       2. 요청 상세 조회
       ========================= */

    /** 입고 번호로 입고 상세 내역 조회 */
    List<Inbound_detailDto> inboundList(int inboundId);

    List<InboundDetailDto> inboundDetailList(int inboundId);

    /** 출고 번호로 출고 상세 내역 조회 */
    List<Outbound_detailDto> outboundList(int outbound_id);

    List<OutboundDetailDto> outboundDetailList(int outboundId);

    /** 입고 상세 번호로 단일 입고 상품 조회 */
    Inbound_detailDto selectDetailIn(int inbound_detail_id);

    /** 출고 상세 번호로 단일 출고 상품 조회 */
    Outbound_detailDto selectDetailOut(int outbound_detail_id);

    String getProductName(@Param("productId") int productId);

    /* =========================
       5. 승인 내역 조회
       ========================= */

    /** 승인 내역 전체 조회 */
    List<ApprovalDto> approvalAll(Map<String,Object> map);

    //field용
    List<ApprovalConfDto> approvalConfIn();

    List<ApprovalConfDto> approvalConfOut();

    int approvalCount(Map<String,Object> map);

    List<ApprovalDto> approvalList(Map<String,Object> map);

    int approvalListCount(@Param("boundType") String boundType);


    List<Inbound_detailDto> inboundAppList(int inbound_id);

    List<Outbound_detailDto> outboundAppList(int outbound_id);

    List<InboundDetail_OfficeDto> selectDetailInOffice(int inbound_id);

    List<OutboundDetail_OfficeDto> selectDetailOutOffice(int outbound_id);
}
