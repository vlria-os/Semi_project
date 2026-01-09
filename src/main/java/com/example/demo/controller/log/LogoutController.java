package com.example.demo.controller.log;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        //String logoutMsg="로그아웃 완료";
        return "redirect:/";
    }
}
