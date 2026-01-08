package com.example.demo.controller;

import com.example.demo.dto.WebuserDTO;
import com.example.demo.service.WebuserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebuserInsertController {
    private final WebuserService service;

    @GetMapping("/webuser/insert")
    public String webuserInsert(){
        return "webuserInsert";
    }

    @GetMapping("/webuser/idcheck")
    @ResponseBody
    public String webuserIdcheck(@RequestParam String id){
        WebuserDTO dto=service.idCheck(id);
        if(dto != null){
            String result="이미 사용 중인 아이디입니다.";
            return result;
        }else {
            String result="사용 가능한 아이디입니다.";
            return result;
        }
    }

    @PostMapping("/webuser/insert")
    public String webuserInsertOk(WebuserDTO dto,
                                  RedirectAttributes r){
        boolean result=service.insertWebuser(dto);
        if(result){
            r.addFlashAttribute("status",
                    "success");
            r.addFlashAttribute("msg",
                    "계정 추가 성공!");
        }else {
            r.addFlashAttribute("status",
                    "failure");
            r.addFlashAttribute("msg",
                    "계정 추가 실패!");
        }
        return "redirect:/webuser/list";
    }
}
