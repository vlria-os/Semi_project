package com.example.demo.controller.temp;

import com.example.demo.dto.WorkingLogDto;
import com.example.demo.service.WorkingLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LayoutController {
    private final WorkingLogService workingLogService;

    @GetMapping("/layout")
    public String home(Model model,
                         HttpSession session){
        if((int)session.getAttribute("role_id")==1){
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/notice");
        }else if((int)session.getAttribute("role_id")==2){
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "content/notice");
        }else if((int)session.getAttribute("role_id")==3){
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "content/notice");
        }

        return "layout";
    }

    @GetMapping("/")
    public String notice(Model model,
                         HttpSession session){
        if(session.getAttribute("webuser_id") == null){
            return "redirect:/login";
        }

        //어제 퇴근 기록 - 있어야 출근 버튼 활성화
        WorkingLogDto checkOutDto=workingLogService.getYesterdayCheckOutLogOne((int)session.getAttribute("webuser_id"));

        //오늘 출근 기록 - 없어야 출근 버튼 활성화 (있으면 퇴근 버튼이 보여야 함)
        WorkingLogDto checkInDto=workingLogService.getTodayCheckInLogOne((int)session.getAttribute("webuser_id"));

        //오늘 퇴근 기록
        WorkingLogDto todayCheckOutDto=workingLogService.getTodayCheckOutLogOne((int)session.getAttribute("webuser_id"));

        if((int)session.getAttribute("role_id")==1){
            session.setAttribute("YesterdayCheckOutLog",checkOutDto);
            session.setAttribute("TodayCheckInLog",checkInDto);
            session.setAttribute("TodayCheckOutLog",todayCheckOutDto);
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/notice");
        }else if((int)session.getAttribute("role_id")==2){
            session.setAttribute("YesterdayCheckOutLog",checkOutDto);
            session.setAttribute("TodayCheckInLog",checkInDto);
            session.setAttribute("TodayCheckOutLog",todayCheckOutDto);
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "content/notice");
        }else if((int)session.getAttribute("role_id")==3){
            session.setAttribute("YesterdayCheckOutLog",checkOutDto);
            session.setAttribute("TodayCheckInLog",checkInDto);
            session.setAttribute("TodayCheckOutLog",todayCheckOutDto);
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "content/notice");
        }

        return "layout";
    }
}
