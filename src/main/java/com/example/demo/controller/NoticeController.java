package com.example.demo.controller;

import com.example.demo.dto.NoticeDto;
import com.example.demo.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    // 목록
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {

        Map<String, Object> result =
                noticeService.getNoticeList(page, startDate, endDate);

        var pinnedNoticeService = noticeService.getPinnedNotices();

        model.addAttribute("noticeList", result.get("list"));    //일반공지
        model.addAttribute("pinnedNotices",pinnedNoticeService); //상단고정 공지
        model.addAttribute("page", page);
        model.addAttribute("hasNext", result.get("hasNext"));
        model.addAttribute("prevPage", page > 1 ? page - 1 : 1);
        model.addAttribute("nextPage", page + 1);

        return "notice/list";
    }

    // 등록 화면
    @GetMapping("/new")
    public String newForm() {
        return "notice/new";
    }

    // 등록 처리
    @PostMapping("/new")
    public String save(NoticeDto notice) {
       if(notice.getPinYn() == null) {
           notice.setPinYn("N");
       }
       noticeService.saveNotice(notice);
        return "redirect:/notice/list";
    }

    // 상세
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("notice",
                noticeService.getNoticeDetail(id));
        return "notice/detail";
    }
    //수정
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("notice",
                noticeService.getNoticeDetail(id));
        return "notice/edit";
    }
    //수정처리 (POST)
     @PostMapping("/edit")
    public String edit(NoticeDto notice) {
        if(notice.getPinYn() == null) {
            notice.setPinYn("N");
        }
        noticeService.updateNotice(notice);
        return "redirect:/notice/detail/" + notice.getNoticeId();
    }
    @GetMapping ("/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notice/list";
    }
}
