package com.example.demo.controller.request;

import com.example.demo.dto.InboundDetailDto;
import com.example.demo.dto.Inbound_detailDto;
import com.example.demo.dto.OutboundDetailDto;
import com.example.demo.service.RequestService;
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
public class DetailController {
    private final RequestService requestService;

    // ===== 관리자 =====

    @GetMapping("/request/admin/inbound")
    @ResponseBody
    public List<InboundDetailDto> adminInbound(
            @RequestParam int inbound_id) {

        return requestService.inboundDetailList(inbound_id);
    }

    @GetMapping("/request/admin/outbound")
    @ResponseBody
    public List<OutboundDetailDto> adminOutbound(
            @RequestParam int outbound_id) {
        return requestService.outboundDetailList(outbound_id);
    }

    // ===== 사용자 =====

    @GetMapping("/request/user/inbound")
    public String userInbound(
            @RequestParam int inbound_id,
            Model model) {

        model.addAttribute("list", requestService.inboundList(inbound_id));
        return "request/Indetail1";
    }

    @GetMapping("/request/user/outbound")
    public String userOutbound(
            @RequestParam int outbound_id,
            Model model) {

        model.addAttribute("list", requestService.outboundList(outbound_id));
        return "request/Outdetail1";
    }
}
