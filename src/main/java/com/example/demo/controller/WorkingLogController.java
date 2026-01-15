package com.example.demo.controller;

import com.example.demo.service.WorkingLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
            return "success";
        }else {
            return "failure";
        }
    }

    @GetMapping("/work/checkOut")
    @ResponseBody
    public String checkOut(HttpSession session){
        int webuserId=(int)session.getAttribute("webuser_id");

        if(session.getAttribute("webuser_id")==null){
            return "failure";
        }

        boolean checkOut=workingLogService.checkOut(webuserId);

        if(checkOut){
            return "success";
        }else {
            return "failure";
        }
    }
}
