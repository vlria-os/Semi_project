package com.example.demo.controller;

import com.example.demo.dto.Inbound_detailDto;
import com.example.demo.dto.Outbound_detailDto;
import com.example.demo.service.OutboundService;
import com.example.demo.service.RequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class outbound_app_Controller {
    private final OutboundService outboundService;
    private final RequestService requestService;

    @GetMapping("/admin/outbound_app")
    public String outbound_appFrom(){
        return "admin/outbound_app";
    }

    @PostMapping("/admin/outbound_app")
    public Map<String, Object> outbound_app(int outbound_id,
                               @ModelAttribute Outbound_detailDto outbound_detailDto,
                               HttpSession session){

        int approver_id=(int)session.getAttribute("webuser_id");
        int n=outboundService.update_approval(outbound_id,outbound_detailDto,approver_id);

        Map<String,Object> map=requestService.outboundList(outbound_id);
        List<Outbound_detailDto> detailList=(List<Outbound_detailDto>) map.get("list");
        Map<Integer,String> names=(Map<Integer,String>) map.get("names");

        Map<String,Object> result = new HashMap<>();
        result.put("success", n>0);
        result.put("detailList", detailList); // 최신 상세 리스트 반환
        result.put("names",names);
        return result;
    }
}
