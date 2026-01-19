package com.example.demo.controller;

import com.example.demo.service.RequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RequestLIstController {
    private final RequestService requestService;

    @GetMapping("/content/requestList_admin")
    public String inboundAll(HttpSession session,
                       Model model,
                       @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
        Integer role_id = (Integer) session.getAttribute("role_id");
        Integer webuser_id = (Integer) session.getAttribute("webuser_id");

        if (role_id == null) {
            return "redirect:/";
        }else if(role_id == 1){
            Map<String,Object> map=requestService.inboundAll(pageNum);

            model.addAttribute("list",map.get("list"));
            model.addAttribute("pageInfo",map.get("pageInfo"));
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/requestList_admin");
        }
        model.addAttribute("webuser_id",webuser_id);
        model.addAttribute("role_id",role_id);
        return "layout";
    }

    @GetMapping("/content/requestList_admin_out")
    public String outboundAll(HttpSession session,
                                        Model model,
                                        @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
        Integer role_id = (Integer) session.getAttribute("role_id");
        Integer webuser_id = (Integer) session.getAttribute("webuser_id");

        if (role_id == null) {
            return "redirect:/";
        }else if(role_id == 1){
            Map<String,Object> map=requestService.outboundAll(pageNum);

            model.addAttribute("list",map.get("list"));
            model.addAttribute("pageInfo",map.get("pageInfo"));
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/requestList_admin_out");
        }
        model.addAttribute("webuser_id",webuser_id);
        model.addAttribute("role_id",role_id);
        return "layout";
    }
}
