package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.WebuserService;
import com.example.demo.service.WorkingLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CalendarController {
    private final WorkingLogService workingLogService;
    private final WebuserService webuserService;

    @GetMapping("/work/calendar")
    public String calendar(Model model,
                           @RequestParam(required = false) Integer targetUserId,
                           @RequestParam(required = false) String returnUrl,
                           HttpSession session){
        Integer myId=(Integer) session.getAttribute("webuser_id");
        Integer roleId=(Integer) session.getAttribute("role_id");
        String myName=(String) session.getAttribute("webuserName");

        int viewUserId=myId;
        String viewUserName=myName;

        if (roleId != null && roleId == 1 && targetUserId != null){
            WebuserDto dto=webuserService.selectWebuser(targetUserId);
            if (dto != null){
                viewUserId = targetUserId;
                viewUserName = dto.getWebuser_name();
            }
        }
        model.addAttribute("viewUserName",viewUserName);
        model.addAttribute("viewUserId",viewUserId);

        if (roleId == 1){
            model.addAttribute("navFragment", "fragment/nav/adminNav");
        } else if (roleId == 2){
            model.addAttribute("navFragment", "fragment/nav/officeNav");
        } else if (roleId == 3){
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
        }

        model.addAttribute("content", "content/Calendar");
        model.addAttribute("returnUrl",returnUrl);
        return "layout";
    }

    @GetMapping("/api/work/calendar")
    @ResponseBody
    public List<CalendarDto> getMyWorkingLog(@RequestParam String startDate,
                                             @RequestParam String endDate,
                                             @RequestParam(required = false) Integer targetUserId,
                                             HttpSession session){
        Integer webuserId = (Integer) session.getAttribute("webuser_id");
        if(session.getAttribute("webuser_id") == null){
            return Collections.emptyList();
        }

        Integer roleId = (Integer) session.getAttribute("role_id");

        LocalDate start=LocalDate.parse(startDate);
        LocalDate end=LocalDate.parse(endDate);

        if (roleId != null && roleId == 1 && targetUserId != null){
            workingLogService.ensureAbsences(targetUserId,start,end);
            List<CalendarDto> list=workingLogService.getMyWorkingLog(targetUserId, startDate, endDate, roleId);
            return list;
        }else {
            workingLogService.ensureAbsences(webuserId,start,end);
            List<CalendarDto> list=workingLogService.getMyWorkingLog(webuserId, startDate, endDate, roleId);
            return list;
        }
    }

    @PatchMapping("/api/work/{workingLogId}/checkout-late")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> updateCheckoutLate(@PathVariable int workingLogId,
                                                                 @RequestBody(required = false) CheckoutLateDto dto,
                                                                 HttpSession session){
        Integer myid = (Integer) session.getAttribute("webuser_id");
        if (myid == null) {
            return ResponseEntity.status(401).body(Map.of("ok", false, "message", "로그인 후 다시 시도하시오!"));
        }

        Integer roleId = (Integer) session.getAttribute("role_id");
        if (roleId == null || roleId != 1) {
            return ResponseEntity.status(403).body(Map.of("ok", false, "message", "관리자만 퇴근 보정이 가능합니다."));
        }

        Integer webuserId=workingLogService.selectWorkingLogOwner(workingLogId);
        if (webuserId == null || webuserId == 0){
            return ResponseEntity.badRequest().body(
                    Map.of("ok", false, "message", "해당 근무기록을 찾을 수 없습니다.")
            );
        }

        String checkoutTime = (dto == null) ? null : dto.getCheckOutTime();
        if (checkoutTime != null) checkoutTime = checkoutTime.trim();

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

    @PatchMapping("/api/work/{workingLogId}/absent-update")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> updateAbsent(@PathVariable int workingLogId,
                                                           @RequestBody AbsentUpdateDto dto,
                                                           HttpSession session){
        Integer webuserId=(Integer) session.getAttribute("webuser_id");
        if (webuserId == null){
            return ResponseEntity.status(403).body(Map.of("ok", false, "message", "로그인 후 다시 시도하시오!"));
        }

        Integer roleId=(Integer) session.getAttribute("role_id");
        if (roleId == null || roleId != 1){
            return ResponseEntity.status(403).body(Map.of("ok", false, "message", "관리자 계정이 아닙니다!"));
        }

        String memo=(String) session.getAttribute("webuserName");

        boolean update=workingLogService.updateAbsent(workingLogId, dto.getAttendStatus(), dto.getCheckInTime(),
                                                        dto.getCheckOutTime(), memo);

        if (update){
            return ResponseEntity.ok(
                    Map.of(
                            "ok", true,
                            "message", "결근 보정 성공!"
                    )
            );
        }else {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "ok", false,
                            "message", "결근 보정 실패!"
                    )
            );
        }

    }
}
