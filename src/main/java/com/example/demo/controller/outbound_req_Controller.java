package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.OutboundService;
import com.example.demo.service.ProductStockService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class outbound_req_Controller {
    private final OutboundService outboundService;
    private final ProductStockService service;

    @GetMapping("/content/outbound_req")
    public String outbound_reqForm(@RequestParam(required = false)String keyword,
                                   @RequestParam(required = false)String status,
                                   Model model){
//        List<ProductStockDto> p = service.selectAll(keyword, status);
//        model.addAttribute("p", p);

        model.addAttribute("navFragment", "fragment/nav/officeNav");
        model.addAttribute("content", "content/outbound_req");
        return "layout";
    }

    @PostMapping("/office_staff/outbound_req")
    public String outbound_req(@ModelAttribute Outbound_reqListDto outbound_reqListDto,
                               HttpSession session,
                               Model model){
        List<Outbound_reqDto> outbound_reqDtos=outbound_reqListDto.getOutbound_reqDtos();
        int webuser_id=(int)session.getAttribute("webuser_id");
        int n=outboundService.insert_request(outbound_reqDtos, webuser_id);

        if(n==1){
            model.addAttribute("result","success");
            model.addAttribute("size",outbound_reqDtos.size());
        }else{
            model.addAttribute("result","failure");
        }

        return "redirect:/content/outbound_req";
    }
}
