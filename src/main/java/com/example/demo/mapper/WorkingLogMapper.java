package com.example.demo.mapper;

import com.example.demo.dto.WorkingLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface WorkingLogMapper {
    //출근 체크
    int checkIn(Map<String,Object> map);

    //퇴근 체크
    int checkOut(@Param("webuserId") int webuserId,
                 @Param("attendStatus") String attendStatus,
                 @Param("memo") String memo);

    //미퇴근 기록 확인
    WorkingLogDto selectOpenWorkingLog(@Param("webuserId") int webuserId);

    //오늘 출근 기록 확인
    int countTodayCheckIn(@Param("webuserId") int webuserId);

    //오늘 퇴근 기록 확인
    WorkingLogDto selectTodayCheckOutLogOne(@Param("webuserId") int webuserId);

    //캘린더에 표시될 본인 근무 기록
    List<WorkingLogDto> selectMyWorkingLog(@Param("webuserId") int webuserId,
                                           @Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    //퇴근 보정
    int updateCheckoutLate(@Param("workingLogId") int workingLogId,
                           @Param("webuserId") int webuserId,
                           @Param("checkOutTime") LocalDateTime checkOutTime);

    LocalDate selectWorkingDateById(@Param("workingLogId") int workingLogId,
                                    @Param("webuserId") int webuserId);

    int selectWorkingLogOwner(@Param("workingLogId") int workingLogId);
}
