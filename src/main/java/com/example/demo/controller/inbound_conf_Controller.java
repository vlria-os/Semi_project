package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.InboundService;
import com.example.demo.service.RequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class inbound_conf_Controller {
    private final InboundService inboundService;
    private final RequestService requestService;

    @GetMapping("/field_staff/bound_conf")
    public String bound_confFrom(Model model){
        List<ApprovalDto> list=requestService.approvalconfAll();

        model.addAttribute("list", list);
        model.addAttribute("navFragment", "fragment/nav/fieldNav");
        model.addAttribute("content", "content/approvalList_field");

        return "layout";
    }

    @GetMapping("/field_staff/bound_conf/detailList")
    @ResponseBody
    public Object approvalDetailList(@RequestParam String bound_type,
                                     @RequestParam int bound_id){
        if("IN".equals(bound_type)){
            List<Inbound_detailDto> list=requestService.inboundAppList(bound_id);
            return list;
        }else {
            List<Outbound_detailDto> list=requestService.outboundAppList(bound_id);
            return list;
        }
    }

    @PostMapping("/field_staff/inbound_conf_app")
    public String inbound_conf_app(@ModelAttribute Lot_inListDto lot_inListDto,
                               HttpSession session){
        int confirmer_id=(int)session.getAttribute("webuser_id");
        List<Lot_inDto> lot_inDtos=lot_inListDto.getLot_inDtos();

        String status="APPROVED";
        int n=inboundService.insert_confirm(lot_inDtos,status,null,confirmer_id);

        return "redirect:/field_staff/bound_conf";
    }

    @PostMapping("/field_staff/inbound_conf_rej")
    public String inbound_conf_rej(@ModelAttribute Lot_inListDto lot_inListDto,
                               String reason,
                               HttpSession session){
        int confirmer_id=(int)session.getAttribute("webuser_id");
        List<Lot_inDto> lot_inDtos=lot_inListDto.getLot_inDtos();
        String status="REJECTED";
        int n=inboundService.insert_confirm(lot_inDtos,status,reason,confirmer_id);

        return "redirect:/field_staff/bound_conf";
    }

}
