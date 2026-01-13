package com.example.demo.controller;

import com.example.demo.dto.Outbound_detailDto;
import com.example.demo.service.OutboundService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class outbound_app_Controller {

    private final OutboundService outboundService;

    @GetMapping("/admin/outbound_app")
    public String outbound_appFrom(){
        return "admin/outbound_app";
    }

    @PostMapping("/admin/outbound_app")
    public String outbound_app(int outbound_id,
                               @ModelAttribute Outbound_detailDto outbound_detailDto,
                               HttpSession session){

        // int approver_id=(int)session.getAttribute("webuser_id"); //검사 필요
        int approver_id=1000; //테스트용
        int n=outboundService.update_approval(outbound_id,outbound_detailDto,approver_id);

        return "redirect:/admin/outbound_app";
    }
}
