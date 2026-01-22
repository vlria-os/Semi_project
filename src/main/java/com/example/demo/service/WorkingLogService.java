package com.example.demo.service;

import com.example.demo.dto.CalendarDto;
import com.example.demo.dto.WebuserDto;
import com.example.demo.dto.WorkingLogDto;
import com.example.demo.mapper.WebuserMapper;
import com.example.demo.mapper.WorkingLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkingLogService {
    private final WorkingLogMapper workingLogMapper;
    private final WebuserMapper webuserMapper;

    //출근 체크
    public boolean checkIn(int webuserId, int roleId){
        WebuserDto dto=webuserMapper.selectWebuser(webuserId);
        Map<String,Object> map=new HashMap<>();
        map.put("webuserId",webuserId);
        map.put("name",dto.getWebuser_name());
        map.put("roleId",roleId);
        return workingLogMapper.checkIn(map) > 0;
    }

    //퇴근 체크
    public boolean checkOut(int webuserId, String attendStatus, String memo){
        return workingLogMapper.checkOut(webuserId, attendStatus, memo) > 0;
    }

    //어제 퇴근 기록
    public WorkingLogDto getYesterdayCheckOutLogOne(int webuserId){
        return workingLogMapper.selectYesterdayCheckOutLogOne(webuserId);
    }

    //오늘 출근 기록
    public WorkingLogDto getTodayCheckInLogOne(int webuserId){
        return workingLogMapper.selectTodayCheckInLogOne(webuserId);
    }

    //오늘 퇴근 기록
    public WorkingLogDto getTodayCheckOutLogOne(int webuserId){
        return workingLogMapper.selectTodayCheckOutLogOne(webuserId);
    }

    //캘린더에 표시될 본인 근무 기록
    public List<CalendarDto> getMyWorkingLog(int webuserId, String startDate, String endDate){
        List<WorkingLogDto> workList=workingLogMapper.selectMyWorkingLog(webuserId,startDate,endDate);

        List<CalendarDto> list=new ArrayList<>();

        for(WorkingLogDto dto:workList){
            CalendarDto newDto=new CalendarDto();
            newDto.setId(String.valueOf(dto.getWorkingLogId()));

            String attendStatus=dto.getAttendStatus();
            boolean isCheckInOnly = (dto.getCheckInTime() != null && dto.getCheckOutTime() ==null);

            String title = "기타";
            String color = "#5F6368";

            if (!isCheckInOnly && attendStatus == null) {
                attendStatus = "ETC"; // 데이터 이상 시 fallback
            }

            if(isCheckInOnly){
                title="출근";
                color="#F4B400";
            } else {

                switch (attendStatus){
                    case "WORK":
                        title = "정상 근무";
                        color = "#1F3C88";
                        break;

                    case "HALF_AM":
                        title = "오전 반차";
                        color = "#7B61FF";
                        break;

                    case "HALF_PM":
                        title = "오후 반차";
                        color = "#7B61FF";
                        break;

                    case "ANNUAL":
                        title = "연차";
                        color = "#7B61FF";
                        break;

                    case "SICK":
                        title = "병가";
                        color = "#D93025";
                        break;

                    case "CHECK_OUT_LATE":
                        title = "익일 퇴근 처리";
                        color = "#000000";
                        break;

                    case "ETC":
                    default:
                        title = "기타";
                        color = "#5F6368";
                }
            }

            newDto.setTitle(title);
            newDto.setBackgroundColor(color);
            newDto.setBorderColor(color);
            newDto.setTextColor("#FFFFFF");
            newDto.setStart(dto.getWorkingDate().toString());
            newDto.setEnd(dto.getWorkingDate().plusDays(1).toString());
            newDto.setAllDay(true);

            Map<String,Object> map=new HashMap<>();
            map.put("attendStatus", isCheckInOnly ? "CHECK_IN_ONLY" : attendStatus);
            map.put("checkInTime", dto.getCheckInTime() == null ? null : dto.getCheckInTime().toLocalTime().toString());
            map.put("checkOutTime",dto.getCheckOutTime() == null ? null : dto.getCheckOutTime().toLocalTime().toString());
            map.put("memo",dto.getMemo() == null ? "" : dto.getMemo());

            //퇴근 보정 버튼 노출 여부 결정하는 조건
            boolean canCheckoutLate = dto.getCanCheckoutLate() != null && dto.getCanCheckoutLate() == 1;

            map.put("canCheckoutLate",canCheckoutLate);

            newDto.setExtendedProps(map);

            list.add(newDto);
        }

        return list;
    }

    //퇴근 보정
    public boolean updateCheckoutLate(int workingLogId, int webuserId, String checkoutTime){
        LocalDate workingDate = workingLogMapper.selectWorkingDateById(workingLogId,webuserId);
        if (workingDate == null) return false;

        LocalTime checkOut;
        if (checkoutTime == null || checkoutTime.isBlank()){
            checkOut = LocalTime.of(18, 0);
        } else {
            checkOut = LocalTime.parse(checkoutTime);
        }

        LocalTime start=LocalTime.of(9,0);
        LocalTime end=LocalTime.of(23,59);
        if(checkOut.isBefore(start) || checkOut.isAfter(end)){
            return false;
        }

        LocalDateTime checkOutTime = workingDate.atTime(checkOut);

        int n=workingLogMapper.updateCheckoutLate(workingLogId, webuserId, checkOutTime);

        return n > 0;
    }
}
