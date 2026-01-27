package com.example.demo.controller.temp;

import com.example.demo.dto.WorkingLogDto;
import com.example.demo.service.NoticeService;
import com.example.demo.service.WorkingLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LayoutController {
    private final WorkingLogService workingLogService;
    private final NoticeService noticeService;

    @GetMapping("/layout")
    public String home(Model model,
                         HttpSession session){
        if((int)session.getAttribute("role_id")==1){
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "notice/list");
        }else if((int)session.getAttribute("role_id")==2){
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "notice/list");
        }else if((int)session.getAttribute("role_id")==3){
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "notice/list");
        }

        return "layout";
    }

    @GetMapping("/")
    public String notice(Model model,
                         HttpSession session){
        if(session.getAttribute("webuser_id") == null){
            return "redirect:/login";
        }

        //미퇴근 기록
        WorkingLogDto openLog=workingLogService.getOpenWorkingLog((int)session.getAttribute("webuser_id"));

        Map<String, Object> result =
                noticeService.getNoticeList(1, null, null, "");
        var pinnedNoticeService = noticeService.getPinnedNotices();

        model.addAttribute("noticeList", result.get("list"));    //일반공지
        model.addAttribute("pinnedNotices",pinnedNoticeService); //상단고정 공지
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("keyword", "");

        if((int)session.getAttribute("role_id")==1){
            session.setAttribute("openLog",openLog);
            session.setAttribute("today", LocalDate.now());
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "/notice/list");
        }else if((int)session.getAttribute("role_id")==2){
            session.setAttribute("openLog",openLog);
            session.setAttribute("today", LocalDate.now());
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "/notice/list");
        }else if((int)session.getAttribute("role_id")==3){
            session.setAttribute("openLog",openLog);
            session.setAttribute("today", LocalDate.now());
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "/notice/list");
        }

        return "layout";
    }
}
