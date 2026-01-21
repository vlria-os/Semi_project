package com.example.demo.controller;

import com.example.demo.dto.CalendarDto;
import com.example.demo.dto.WorkingLogDto;
import com.example.demo.service.WorkingLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalendarController {
    private final WorkingLogService workingLogService;

    @GetMapping("/work/calendar")
    public String calendar(){
        return "content/Calendar";
    }

    @GetMapping("/api/work/calendar")
    @ResponseBody
    public List<CalendarDto> getMyWorkingLog(@RequestParam String startDate,
                                             @RequestParam String endDate,
                                             HttpSession session){
        if(session.getAttribute("webuser_id") == null){
            return Collections.emptyList();
        }

        Integer webuserId = (Integer) session.getAttribute("webuser_id");

        List<CalendarDto> list=workingLogService.getMyWorkingLog(webuserId, startDate, endDate);
        return list;
    }
}
