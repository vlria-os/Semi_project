package com.example.demo.controller;

import com.example.demo.dto.ApprovalConfDto;
import com.example.demo.dto.Lot_outDto;
import com.example.demo.dto.Lot_outListDto;
import com.example.demo.service.OutboundService;
import com.example.demo.service.Ref_ExpService;
import com.example.demo.service.RequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class outbound_conf_Controller {
    private final OutboundService outboundService;
    private final RequestService requestService;
    private final Ref_ExpService refExpService;

    @GetMapping("/field_staff/outbound_conf")
    public String outbound_confFrom(Model model,
                                    @RequestParam(name = "pageNum", defaultValue = "1") int pageNum){
        Map<String,Object> map=requestService.getApprovalConfOut(pageNum);

        model.addAttribute("list", map.get("list"));
        model.addAttribute("pageInfo", map.get("pageInfo"));
        model.addAttribute("navFragment", "fragment/nav/fieldNav");
        model.addAttribute("content", "content/approvalList_field_out");

        return "layout";
    }

    @PostMapping("/field_staff/outbound_conf_app")
    public String outbound_conf_app(int outbound_detail_id,
                               HttpSession session){
        int confirmer_id=(int)session.getAttribute("webuser_id");
        int n=outboundService.insert_confirm(outbound_detail_id,"CONFIRMED",null,confirmer_id);
        if(n<0){
            refExpService.insert_expiration();
        }
        return "redirect:/field_staff/outbound_conf";
    }

    @PostMapping("/field_staff/outbound_conf_rej")
    public String outbound_conf_rej(int outbound_detail_id,
                                String reason,
                                HttpSession session){
        int confirmer_id=(int)session.getAttribute("webuser_id");
        int n=outboundService.insert_confirm(outbound_detail_id,"REJECTED",reason,confirmer_id);

        return "redirect:/field_staff/outbound_conf";
    }
}
