package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.OutboundService;
import com.example.demo.service.ProductStockService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class outbound_req_Controller {
    private final OutboundService outboundService;
    private final ProductStockService service;
    private final ProductStockService productStockService;

    @GetMapping("/content/outbound_req")
    public String outbound_reqForm(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "status", required = false) String status,
                                   Model model){
        Map<String,Object> map=productStockService.selectAll(pageNum, keyword, status);

        model.addAttribute("p",map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("content", "content/outbound_req");
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);

        model.addAttribute("navFragment", "fragment/nav/officeNav");
        model.addAttribute("content", "content/outbound_req");
        return "layout";
    }

    @PostMapping("/content/outbound_req")
    @ResponseBody
    public Map<String,Object> outbound_req(@ModelAttribute Outbound_reqListDto outbound_reqListDto,
                               HttpSession session){
        List<Outbound_reqDto> outbound_reqDtos=outbound_reqListDto.getOutbound_reqDtos();
        int webuser_id=(int)session.getAttribute("webuser_id");
        int n=outboundService.insert_request(outbound_reqDtos, webuser_id);

        Map<String,Object> map=new HashMap<>();
        if(n==1){
            map.put("result","success");
            map.put("size",outbound_reqDtos.size());
        }else{
            map.put("result","failure");
        }

        map.put("navFragment", "fragment/nav/officeNav");
        map.put("content", "content/outbound_req");
        return map;
    }
}
