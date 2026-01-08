package com.example.demo.controller.request;

import com.example.demo.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DetailController {
    private final RequestService service;

    // ===== 관리자 =====

    @GetMapping("/request/admin/inbound")
    public String adminInbound(
            @RequestParam int inbound_id,
            Model model) {

        model.addAttribute("list", service.inboundList(inbound_id));
        return "request/Indetail";
    }

    @GetMapping("/request/admin/outbound")
    public String adminOutbound(
            @RequestParam int outbound_id,
            Model model) {

        model.addAttribute("list", service.outboundList(outbound_id));
        return "request/Outdetail";
    }

    // ===== 사용자 =====

    @GetMapping("/request/user/inbound")
    public String userInbound(
            @RequestParam int inbound_id,
            Model model) {

        model.addAttribute("list", service.inboundList(inbound_id));
        return "request/Indetail1";
    }

    @GetMapping("/request/user/outbound")
    public String userOutbound(
            @RequestParam int outbound_id,
            Model model) {

        model.addAttribute("list", service.outboundList(outbound_id));
        return "request/Outdetail1";
    }
}
