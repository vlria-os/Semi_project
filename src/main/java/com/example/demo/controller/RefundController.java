package com.example.demo.controller;

import com.example.demo.dto.RefundDto;
import com.example.demo.service.Ref_ExpService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RefundController {
    private final Ref_ExpService ref_ExpService;

    @PostMapping("/refund")
    @ResponseBody
    public Map<String, Object> refund_req(int outbound_detail_id,
                                          HttpSession session){
        int n=ref_ExpService.insert_refund(outbound_detail_id, (int)session.getAttribute("webuser_id"));
        Map<String, Object> result = new HashMap<>();
        System.out.println("===============>"+n);
        if(n==-1){
            result.put("success", "이미 반품 요청된 항목입니다.");
        }else{
            result.put("success", true);
        }

        return result;
    }
}
