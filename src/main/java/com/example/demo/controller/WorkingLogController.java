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
        return "";
    }
}
