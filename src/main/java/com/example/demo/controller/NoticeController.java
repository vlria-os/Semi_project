package com.example.demo.controller;

import com.example.demo.dto.NoticeDto;
import com.example.demo.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    return "redirect:/notice/new";
}
}
