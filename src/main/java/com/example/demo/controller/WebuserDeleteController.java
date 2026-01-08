package com.example.demo.controller;

import com.example.demo.service.WebuserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebuserDeleteController {
    private final WebuserService service;
    @GetMapping("/webuser/delete")
    public String webuserDelete(@RequestParam("webuser_id") int webuser_id,
                                 RedirectAttributes r){
        boolean result= service.deleteWebuser(webuser_id);
        if(result){
            r.addFlashAttribute("status",
                    "success");
            r.addFlashAttribute("msg",
                    "계정 삭제 성공!");
        }else {
            r.addFlashAttribute("status",
                    "failure");
            r.addFlashAttribute("msg",
                    "계정 삭제 실패!");
        }
        return "redirect:/webuser/list";
    }
}
