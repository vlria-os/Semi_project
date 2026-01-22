package com.example.demo.controller;

import com.example.demo.dto.CalendarDto;
import com.example.demo.dto.CheckoutLateDto;
import com.example.demo.dto.WorkingLogDto;
import com.example.demo.service.WorkingLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @PatchMapping("/api/work/{workingLogId}/checkout-late")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> updateCheckoutLate(@PathVariable int workingLogId,
                                                                 @RequestBody(required = false) CheckoutLateDto dto,
                                                                 HttpSession session){
        if(session.getAttribute("webuser_id") == null){
            return ResponseEntity.status(401).body(
                    Map.of(
                            "ok", false,
                            "message", "로그인 후 다시 시도하시오!"
                    )
            );
        }
        int webuserId = (int) session.getAttribute("webuser_id");

        String checkoutTime= (dto == null) ? null : dto.getCheckOutTime();
        boolean update=workingLogService.updateCheckoutLate(workingLogId, webuserId, checkoutTime);

        if(update){
            return ResponseEntity.ok(
                    Map.of(
                            "ok", true,
                            "message", "퇴근 보정 성공!"
                    )
            );
        } else {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "ok", false,
                            "message", "퇴근 보정 실패!"
                    )
            );
        }
    }
}
