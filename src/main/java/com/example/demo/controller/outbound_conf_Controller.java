package com.example.demo.controller;

import com.example.demo.dto.Lot_outDto;
import com.example.demo.dto.Lot_outListDto;
import com.example.demo.service.OutboundService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class outbound_conf_Controller {
    private final OutboundService outboundService;

    @GetMapping("/field_staff/outbound_conf")
    public String outbound_confFrom(){
        return "field_staff/outbound_conf";
    }

    @PostMapping("/field_staff/outbound_conf_app")
    public String outbound_conf_app(int outbound_detail_id,
                               HttpSession session){
        int confirmer_id=(int)session.getAttribute("webuser_id");
        int n=outboundService.insert_confirm(outbound_detail_id,"CONFIRMED",null,confirmer_id);

        return "redirect:/field_staff/bound_conf";
    }

    @PostMapping("/field_staff/outbound_conf_rej")
    public String outbound_conf_rej(int outbound_detail_id,
                                String reason,
                                HttpSession session){
        int confirmer_id=(int)session.getAttribute("webuser_id");
        int n=outboundService.insert_confirm(outbound_detail_id,"REJECTED",reason,confirmer_id);

        return "redirect:/field_staff/bound_conf";
    }
}
