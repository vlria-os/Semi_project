package com.example.demo.controller;

import com.example.demo.dto.NoticeDto;
import com.example.demo.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
private final NoticeService noticeService;
    //등록화면
@GetMapping("/new")
public String newForm() {
    return "notice/new";
}
//insert 실행
@PostMapping("/new")
public String insert(NoticeDto notice) {
    noticeService.insertNotice(notice);
    return "redirect:/notice/list";
}
@GetMapping("/notice")
    public String list(
            @RequestParam(defaultValue =  "1") int page,
            @RequestParam(required = false) String keyword,
            Model model) {
    Map<String,Object >result = noticeService.getNoticeList(page,keyword);
    model.addAttribute(result);
    model.addAttribute("keyword", keyword);
    return  "notice/list";
}
@GetMapping ("/notice/{id}")
    public String daeail(@PathVariable long id, Model model) {
    model.addAttribute("notice",noticeService.getNoteceDetail(id));
    return "notice/detail";
}
@GetMapping ("/list")
    public String list(Model model) {
    model.addAttribute("list", Collections.emptyList());
    return "notice/list";
}

}
