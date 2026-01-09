package com.example.demo.controller.temp;

import jakarta.servlet.http.HttpSession;
import org.springframework.boot.web.server.servlet.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/layout")
    public String home(){
        return "layout";
    }

    @GetMapping("/notice")
    public String notice(Model model,
                         HttpSession session){
        if((int)session.getAttribute("role_id")==1){
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/notice");
        }else{
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "content/notice");
        }

        return "layout";
    }
}
