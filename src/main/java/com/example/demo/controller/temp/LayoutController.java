package com.example.demo.controller.temp;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LayoutController {
    @GetMapping("/layout")
    public String home(Model model,
                         HttpSession session){
        if((int)session.getAttribute("role_id")==1){
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/notice");
        }else if((int)session.getAttribute("role_id")==2){
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "content/notice");
        }else if((int)session.getAttribute("role_id")==3){
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "content/notice");
        }

        return "layout";
    }

    @GetMapping("/")
    public String notice(Model model,
                         HttpSession session){
        if(session.getAttribute("webuser_id") == null){
            return "redirect:/login";
        }

        if((int)session.getAttribute("role_id")==1){
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/notice");
        }else if((int)session.getAttribute("role_id")==2){
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "content/notice");
        }else if((int)session.getAttribute("role_id")==3){
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "content/notice");
        }

        return "layout";
    }
}
