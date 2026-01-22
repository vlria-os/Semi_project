package com.example.demo.controller;

import com.example.demo.dto.CheckOutDto;
import com.example.demo.dto.WorkingLogDto;
import com.example.demo.service.WorkingLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class WorkingLogController {
    private final WorkingLogService workingLogService;

    @GetMapping("/work/checkIn")
    @ResponseBody
    public String checkIn(HttpSession session){
        int webuserId=(int)session.getAttribute("webuser_id");

        if(session.getAttribute("webuser_id")==null){
            return "failure";
        }

        int roleId=(int)session.getAttribute("role_id");
        boolean checkIn=workingLogService.checkIn(webuserId,roleId);

        if(checkIn){
            WorkingLogDto checkInDto=workingLogService.getTodayCheckInLogOne(webuserId);
            session.setAttribute("checkInLog",checkInDto);
            return "success";
        }else {
            return "failure";
        }
    }

    @PostMapping("/work/checkOut")
    @ResponseBody
    public String checkOut(HttpSession session,
                           @RequestBody CheckOutDto dto){

        if(session.getAttribute("webuser_id")==null){
            return "failure";
        }

        int webuserId=(int)session.getAttribute("webuser_id");
        String attendStatus= dto.getAttendStatus();
        String memo=dto.getMemo();

        boolean checkOut=workingLogService.checkOut(webuserId, attendStatus, memo);

        if(checkOut){
            return "success";
        }else {
            return "failure";
        }
    }
}
