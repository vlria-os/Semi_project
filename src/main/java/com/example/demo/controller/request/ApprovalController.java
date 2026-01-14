package com.example.demo.controller.request;

import com.example.demo.dto.*;
import com.example.demo.service.RequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ApprovalController {
    private final RequestService requestService;

    @GetMapping("/approval/list")
    public String approvalList(Model model,
                               @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "field", required = false) String field,
                               @RequestParam(value = "keyword", required = false) String keyword){
        Map<String,Object> map=requestService.approvalAll(pageNum, field, keyword);

        model.addAttribute("list", map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "content/approvalList");

        return "layout";
    }

    @GetMapping("/approval/list/btn")
    @ResponseBody
    public Map<String,Object> approvalBtn(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "boundType", required = false) String boundType){
        Map<String,Object> map=requestService.approvalList(pageNum,boundType);
        return map;
    }

    @GetMapping("/approval/detailList")
    @ResponseBody
    public Object approvalDetailList(@RequestParam String bound_type,
                                     @RequestParam int bound_id){
        if("IN".equals(bound_type)){
            Map<String,Object> map=requestService.inboundList(bound_id);
            List<Inbound_detailDto> list=(List<Inbound_detailDto>) map.get("list");
            return list;
        }else {
            Map<String,Object> map=requestService.outboundList(bound_id);
            List<Outbound_detailDto> list=(List<Outbound_detailDto>) map.get("list");
            return list;
        }
    }

    @GetMapping("/approval/showReason")
    @ResponseBody
    public String showReason(@RequestParam String bound_type,
                             @RequestParam int detail_id){
        if("IN".equals(bound_type)){
            Inbound_detailDto dto=requestService.selectDetailIn(detail_id);
            String reason=dto.getReason();
            return reason;
        }else {
            Outbound_detailDto dto=requestService.selectDetailOut(detail_id);
            String reason=dto.getReason();
            return reason;
        }
    }
}
