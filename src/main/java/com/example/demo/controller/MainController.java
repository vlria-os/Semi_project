package com.example.demo.controller;

import com.example.demo.dto.BoundDTO;
import com.example.demo.dto.InboundDTO;
import com.example.demo.dto.OutboundDTO;
import com.example.demo.service.RequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RequestService service;

    @GetMapping("/main")
    public String main(HttpSession session, Model model,
                       @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
        Integer role_id = (Integer) session.getAttribute("role_id");
        Integer webuser_id = (Integer) session.getAttribute("webuser_id");

        if (role_id == null) {
            return "redirect:/login";
        }else if(role_id == 1){
            Map<String,Object> map=service.adminBound(pageNum);

            model.addAttribute("list",map.get("list"));
            model.addAttribute("pageInfo",map.get("pageInfo"));
        }else {
            if (webuser_id == null) {
                return "redirect:/login";
            }

            Map<String,Object> map=service.userBound(pageNum,webuser_id);

            model.addAttribute("list",map.get("list"));
            model.addAttribute("pageInfo",map.get("pageInfo"));
        }
        model.addAttribute("webuser_id",webuser_id);
        model.addAttribute("role_id",role_id);
        return "main";
    }

    @GetMapping("/goinbound")
    @ResponseBody
    public Map<String,Object> goinbound(HttpSession session,
                                      Model model,
                                      @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
        int role_id = (int) session.getAttribute("role_id");
        int webuser_id = (int) session.getAttribute("webuser_id");
        if(role_id == 1){
            Map<String,Object> map=service.inboundAll(pageNum);
            return map;
        }else {
            Map<String,Object> map=service.selectInbound(pageNum,webuser_id);
            return map;
        }

    }

    @GetMapping("/gooutbound")
    @ResponseBody
    public Map<String,Object> gooutbound(HttpSession session,
                                        @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
        int role_id = (int) session.getAttribute("role_id");
        int webuser_id = (int) session.getAttribute("webuser_id");
        if(role_id == 1){
            Map<String,Object> map=service.outboundAll(pageNum);
            return map;
        }else {
            Map<String,Object> map=service.selectOutbound(pageNum,webuser_id);
            return map;
        }
    }
}
