package com.example.demo.controller;

import com.example.demo.dto.WebuserDTO;
import com.example.demo.service.WebuserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebuserListController {
    private final WebuserService service;

    @GetMapping("/webuser/list")
    public String webuserList(HttpSession session,
                              @RequestParam(name = "pageNum",defaultValue = "1") int pageNum,
                              @RequestParam(value = "field", required = false) String field,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              Model model){
       String error = (String) session.getAttribute("error");

        // 역할 검색일 때만 문자열 → 숫자 변환
        if ("role_id".equals(field) && keyword != null && !keyword.isBlank()) {
            if (keyword.equals("관리자")) {
                keyword = String.valueOf(1);
            } else if (keyword.equals("사무 직원")) {
                keyword = String.valueOf(2);
            } else if (keyword.equals("현장 직원")) {
                keyword = String.valueOf(3);
            }
        }

        Map<String,Object> map=service.webuserList(pageNum,field,keyword);
        model.addAttribute("list",map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("field",field);
        model.addAttribute("keyword",keyword);

        return "webuserList";
    }
}
