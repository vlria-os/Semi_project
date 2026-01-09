package com.example.demo.controller.request;

import com.example.demo.dto.Inbound_detailDto;
import com.example.demo.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DetailController {
    private final RequestService service;

    // ===== 관리자 =====

    @GetMapping("/request/admin/inbound")
    @ResponseBody
    public List<Inbound_detailDto> adminInbound(
            @RequestParam int inbound_id,
            Model model) {

        return service.inboundList(inbound_id);
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
