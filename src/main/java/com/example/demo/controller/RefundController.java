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

    @GetMapping("/office_staff/refund")
    @ResponseBody
    public Map<String, Object> refund_req(int lot_out_id,
                                          HttpSession session){
        int n=ref_ExpService.insert_refund(lot_out_id, (int)session.getAttribute("webuser_id"));
        Map<String, Object> result = new HashMap<>();

        if(n==-1){
            result.put("success", false);
        }else{
            result.put("success", true);
        }

        return result;
    }
}
