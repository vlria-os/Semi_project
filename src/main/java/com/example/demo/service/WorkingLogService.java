package com.example.demo.service;

import com.example.demo.dto.CalendarDto;
import com.example.demo.dto.WebuserDto;
import com.example.demo.dto.WorkingLogDto;
import com.example.demo.mapper.WebuserMapper;
import com.example.demo.mapper.WorkingLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkingLogService {
    private final WorkingLogMapper workingLogMapper;
    private final WebuserMapper webuserMapper;

    //출근 체크
    public boolean checkIn(int webuserId, int roleId){
        int cnt=workingLogMapper.countTodayCheckIn(webuserId);

        if (cnt > 0){
            return false;
        }

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

    //미퇴근 기록
    public WorkingLogDto getOpenWorkingLog(int webuserId){
        return workingLogMapper.selectOpenWorkingLog(webuserId);
    }

    //오늘 출근 기록
    public int getCountTodayCheckIn(int webuserId){
        return workingLogMapper.countTodayCheckIn(webuserId);
    }

    //오늘 퇴근 기록
    public WorkingLogDto getTodayCheckOutLogOne(int webuserId){
        return workingLogMapper.selectTodayCheckOutLogOne(webuserId);
    }

    //캘린더에 표시될 본인 근무 기록
    public List<CalendarDto> getMyWorkingLog(int webuserId, String startDate, String endDate, int roleId){
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

                    case "ABSENT":
                        title = "결근";
                        color = "#FF69B4"; // 예: 빨강 (원하는 색으로)
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
            boolean isAdmin=(roleId == 1);

            boolean canCheckoutLate = isAdmin && dto.getCanCheckoutLate() != null && dto.getCanCheckoutLate() == 1;

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

    //결근 수정
    public boolean updateAbsent(int workingLogId, String attendStatus,
                                String checkInTime, String checkOutTime, String memo){
        int ownerId=workingLogMapper.selectWorkingLogOwner(workingLogId);
        if (ownerId <= 0) return false;

        LocalDate workingDate=workingLogMapper.selectWorkingDateById(workingLogId, ownerId);
        if (workingDate == null) return false;

        if (!workingDate.isBefore(LocalDate.now())) return false; // 오늘/미래 수정 금지

        boolean isWork="WORK".equals(attendStatus);

        LocalDateTime CheckInTime;
        LocalDateTime CheckOutTime;

        if (!isWork){
            CheckInTime=null;
            CheckOutTime=null;
        } else {
            LocalTime in;
            if (checkInTime == null || checkInTime.isBlank()){
                in=LocalTime.of(9,0);
            }else {
                in=LocalTime.parse(checkInTime);
            }

            LocalTime out;
            if (checkOutTime == null || checkOutTime.isBlank()){
                out=LocalTime.of(18,0);
            }else {
                out=LocalTime.parse(checkOutTime);
            }

            if (!out.isAfter(in)) return false;

            LocalTime start=LocalTime.of(9,0);
            LocalTime end=LocalTime.of(23,59);
            if (in.isBefore(start) || in.isAfter(end) || out.isBefore(start) || out.isAfter(end)){
                return false;
            }

            CheckInTime=workingDate.atTime(in);
            CheckOutTime=workingDate.atTime(out);
        }

        int n=workingLogMapper.absentUpdate(attendStatus, CheckInTime, CheckOutTime, workingLogId, ownerId, memo);

        return n > 0;
    }

    //근무 기록 주인 찾어
    public int selectWorkingLogOwner(int workingLogId){
        return workingLogMapper.selectWorkingLogOwner(workingLogId);
    }

    //출퇴근 기록 없는 과거의 날짜들 결근 처리
    public void ensureAbsences(int webuserId, LocalDate startDate, LocalDate endDate){
        //근무 있는 날들 받아오기
        Set<LocalDate> exists=workingLogMapper.selectWorkingDates(webuserId, startDate.toString(), endDate.toString())
                        .stream().map(LocalDate::parse).collect(Collectors.toSet());

        LocalDate today=LocalDate.now();

        LocalDate joined=workingLogMapper.selectUserCreatedDate(webuserId);
        if (joined == null) return;

        LocalDate from=startDate;
        if (joined.isAfter(from)) from = joined;
        for (LocalDate day=from; day.isBefore(endDate); day=day.plusDays(1)){
            //어제까지만 (오늘/미래는 결근 처리하면 안됨)
            if (!day.isBefore(today)) continue;

            //주말 제외
            if (isWeekend(day)) continue;

            //이미 기록 있는 날이면 스킵
            if (exists.contains(day)) continue;

            //결근 처리
            workingLogMapper.insertAbsent(webuserId, day.toString());
        }
    }

    //주말인지 체크
    private boolean isWeekend(LocalDate d){
        return d.getDayOfWeek() == DayOfWeek.SATURDAY
                || d.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

}
