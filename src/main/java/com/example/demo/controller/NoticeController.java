package com.example.demo.controller;

import com.example.demo.dto.NoticeDto;
import com.example.demo.dto.NoticeReplyDto;
import com.example.demo.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    // 목록
    @GetMapping("/notice/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "") String keyword,
            HttpSession session,
            Model model) {

        Map<String, Object> result =
                noticeService.getNoticeList(pageNum, startDate, endDate, keyword);

        var pinnedNoticeService = noticeService.getPinnedNotices();

        model.addAttribute("noticeList", result.get("list"));    //일반공지
        model.addAttribute("pinnedNotices",pinnedNoticeService); //상단고정 공지
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("keyword", keyword);

        Integer roleId = (Integer) session.getAttribute("role_id");
        String nav= switch (roleId != null ? roleId : 1){
            case 2 -> "officeNav";
            case 3 -> "fieldNav";
            default -> "adminNav";
        };

        model.addAttribute("navFragment", "fragment/nav/" + nav);
        model.addAttribute("content", "notice/list");

        return "layout";
    }

    // 등록 화면
    @GetMapping("/notice/new")
    public String newForm(Model model) {


        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "notice/new");
        return "layout";
    }

    // 등록 처리
    @PostMapping("/notice/new")
    public String save(NoticeDto notice, HttpSession session) {
        Integer roleId= (Integer) session.getAttribute("role_id");
        if(roleId == null || roleId != 1){
            notice.setPinYn("N");
        }else if(notice.getPinYn() == null){
            notice.setPinYn("N");
        }

       noticeService.saveNotice(notice);
        return "redirect:/notice/list";
    }

    // 상세
    @GetMapping("/notice/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        model.addAttribute("notice", noticeService.getNoticeDetail(id));
        model.addAttribute("replies", noticeService.getRepliesByNoticeId(id));


        Integer roleId = (Integer) session.getAttribute("role_id");
        String nav= switch (roleId != null ? roleId : 1){
            case 2 -> "officeNav";
            case 3 -> "fieldNav";
            default -> "adminNav";
        };

        model.addAttribute("navFragment", "fragment/nav/" + nav);
        model.addAttribute("content", "notice/detail");
        return "layout";
    }
    //수정
    @GetMapping("/notice/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.getNoticeDetail(id));
        return "notice/edit";
    }
    //수정처리 (POST)
     @PostMapping("/notice/edit")
    public String edit(NoticeDto notice) {
        if(notice.getPinYn() == null) {
            notice.setPinYn("N");
        }
        noticeService.updateNotice(notice);
        return "redirect:/notice/detail/" + notice.getNoticeId();
    }
    @GetMapping ("/notice/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notice/list";
    }

    @PostMapping("/notice/reply/new")
    public String saveReply(NoticeReplyDto replyDto){
        noticeService.saveReply(replyDto);
        return "redirect:/notice/detail/" + replyDto.getNoticeId();
    }

    @GetMapping("/notice/reply/delete/{replyId}/{noticeId}")
    public String deleteReply(@PathVariable Long replyId, @PathVariable Long noticeId){
        noticeService.deleteReply(replyId);
        return "redirect:/notice/detail//" + noticeId;
    }

    @PostMapping("/notice/reply/edit")
    public String updateReply(NoticeReplyDto replyDto){
        noticeService.updateReply(replyDto);
        return "redirect:/notice/detail/" + replyDto.getNoticeId();
    }
}
