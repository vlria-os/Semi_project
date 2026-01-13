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

    @GetMapping("/request/approvalIn")
    public String approvalIn(@RequestParam("inbound_detail_id") int inbound_detail_id,
                             @RequestParam("inbound_id") int inbound_id,
                             HttpSession session,
                             RedirectAttributes r){
        int approver_id = (int) session.getAttribute("webuser_id");
        String approval_status="approved";

        ApprovalDto dto=requestService.selectApprovalIn(inbound_id);
        if(dto != null){
            boolean result2=requestService.inboundDetailStatus(inbound_detail_id,approval_status,null);
            if(result2){
                Map<String,Object> map=requestService.inboundList(inbound_id);
                List<Inbound_detailDto> list=(List<Inbound_detailDto>) map.get("list");

                int count=0;
                int a=list.size();
                for(Inbound_detailDto d:list){
                    if("approved".equals(d.getApproval_status())){
                        count++;
                    }
                }
                if(a > 0 && count == a){
                    requestService.inboundStatus(inbound_id,approval_status);
                }

                r.addFlashAttribute("status","sucess");
                r.addFlashAttribute("msg","입고 요청 승인 성공!");
                return "redirect:/main";
            }else {
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","입고 요청 승인 후 상태 변경 실패!");
                return "redirect:/main";
            }
        }else {
            boolean result= requestService.approvalIn(approver_id,inbound_id,approval_status);
            if(result){
                boolean result2=requestService.inboundDetailStatus(inbound_detail_id,approval_status,null);
                if(result2){
                    Map<String,Object> map=requestService.inboundList(inbound_id);
                    List<Inbound_detailDto> list=(List<Inbound_detailDto>) map.get("list");

                    int count=0;
                    int a=list.size();
                    for(Inbound_detailDto d:list){
                        if("approved".equals(d.getApproval_status())){
                            count++;
                        }
                    }
                    if(a > 0 && count == a){
                        requestService.inboundStatus(inbound_id,approval_status);
                    }

                    r.addFlashAttribute("status","sucess");
                    r.addFlashAttribute("msg","입고 요청 승인 성공!");
                    return "redirect:/main";
                }else {
                    r.addFlashAttribute("status","failure");
                    r.addFlashAttribute("msg","입고 요청 승인 후 상태 변경 실패!");
                    return "redirect:/main";
                }
            }else {
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","입고 요청 승인 실패!");
                return "redirect:/main";
            }
        }
    }

    @GetMapping("/request/approvalOut")
    public String approvalOut(@RequestParam("outbound_detail_id") int outbound_detail_id,
                              @RequestParam("outbound_id") int outbound_id,
                              HttpSession session,
                              RedirectAttributes r){
        int approver_id = (int) session.getAttribute("webuser_id");
        String approval_status="approved";

        ApprovalDto dto=requestService.selectApprovalOut(outbound_id);
        if(dto != null){
            boolean result2=requestService.outboundDetailStatus(outbound_detail_id,approval_status,null);
            if(result2){
                Map<String,Object> map=requestService.outboundList(outbound_id);
                List<Outbound_detailDto> list=(List<Outbound_detailDto>) map.get("list");

                int count=0;
                int a=list.size();
                for(Outbound_detailDto d:list){
                    if("approved".equals(d.getApproval_status())){
                        count++;
                    }
                }
                if(a > 0 && count == a){
                    requestService.outboundStatus(outbound_id,approval_status);
                }

                r.addFlashAttribute("status","sucess");
                r.addFlashAttribute("msg","출고 요청 승인 성공!");
                return "redirect:/main";
            }else {
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","출고 요청 승인 후 상태 변경 실패!");
                return "redirect:/main";
            }
        }else {
            boolean result= requestService.approvalOut(approver_id,outbound_id,approval_status);
            if(result){
                boolean result2=requestService.outboundDetailStatus(outbound_detail_id,approval_status,null);
                if(result2){
                    Map<String,Object> map=requestService.outboundList(outbound_id);
                    List<Outbound_detailDto> list=(List<Outbound_detailDto>) map.get("list");

                    int count=0;
                    int a=list.size();
                    for(Outbound_detailDto d:list){
                        if("approved".equals(d.getApproval_status())){
                            count++;
                        }
                    }
                    if(a > 0 && count == a){
                        requestService.outboundStatus(outbound_id,approval_status);
                    }

                    r.addFlashAttribute("status","sucess");
                    r.addFlashAttribute("msg","출고 요청 승인 성공!");
                    return "redirect:/main";
                }else {
                    r.addFlashAttribute("status","failure");
                    r.addFlashAttribute("msg","출고 요청 승인 후 상태 변경 실패!");
                    return "redirect:/main";
                }
            }else {
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","출고 요청 승인 실패!");
                return "redirect:/main";
            }
        }
    }

    @PostMapping("/request/rejectionIn")
    public String rejectionIn(@RequestParam int inbound_detail_id,
                              @RequestParam int inbound_id,
                              @RequestParam String reason,
                              HttpSession session,
                              RedirectAttributes r){
        int approver_id = (int) session.getAttribute("webuser_id");
        String approval_status="rejected";

        ApprovalDto dto=requestService.selectApprovalIn(inbound_id);
        if(dto != null){

            boolean update=requestService.updateApprovalIn(inbound_id);

            if(update){
                boolean result2=requestService.inboundDetailStatus(inbound_detail_id,approval_status,reason);
                if(result2){
                    InboundDto d=requestService.selectInboundId(inbound_id);
                    if(!"rejected".equals(d.getApproval_status())){
                        boolean result3=requestService.inboundStatus(inbound_id,approval_status);
                        if(result3){
                            r.addFlashAttribute("status","success");
                            r.addFlashAttribute("msg","입고 요청 반려 성공!");
                            return "redirect:/main";
                        }else {
                            r.addFlashAttribute("status","failure");
                            r.addFlashAttribute("msg","입고 요청 반려 후 상태 변경 실패!");
                            return "redirect:/main";
                        }
                    }else {
                        r.addFlashAttribute("status","success");
                        r.addFlashAttribute("msg","입고 요청 반려 성공!");
                        return "redirect:/main";
                    }
                }else {
                    r.addFlashAttribute("status","failure");
                    r.addFlashAttribute("msg","입고 요청 반려 후 상태 변경 실패!");
                    return "redirect:/main";
                }
            }else {
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","입고 요청 반려 실패!");
                return "redirect:/main";
            }
        }else {
            boolean result=requestService.rejectionIn(approver_id,inbound_id,approval_status);
            if(result){
                boolean result2=requestService.inboundDetailStatus(inbound_detail_id,approval_status,reason);
                if(result2){
                    InboundDto d=requestService.selectInboundId(inbound_id);
                    if(!"rejected".equals(d.getApproval_status())){
                        boolean result3=requestService.inboundStatus(inbound_id,approval_status);
                        if(result3){
                            r.addFlashAttribute("status","success");
                            r.addFlashAttribute("msg","입고 요청 반려 성공!");
                            return "redirect:/main";
                        }else {
                            r.addFlashAttribute("status","failure");
                            r.addFlashAttribute("msg","입고 요청 반려 후 상태 변경 실패!");
                            return "redirect:/main";
                        }
                    }else {
                        r.addFlashAttribute("status","success");
                        r.addFlashAttribute("msg","입고 요청 반려 성공!");
                        return "redirect:/main";
                    }
                }else {
                    r.addFlashAttribute("status","failure");
                    r.addFlashAttribute("msg","입고 요청 반려 후 상태 변경 실패!");
                    return "redirect:/main";
                }
            }else {
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","입고 요청 반려 실패!");
                return "redirect:/main";
            }
        }
    }

    @PostMapping("/request/rejectionOut")
    public String rejectionOut(@RequestParam int outbound_detail_id,
                              @RequestParam int outbound_id,
                              @RequestParam String reason,
                              HttpSession session,
                              RedirectAttributes r){
        int approver_id = (int) session.getAttribute("webuser_id");
        String approval_status="rejected";

        ApprovalDto dto=requestService.selectApprovalOut(outbound_id);
        if(dto != null){

            boolean update=requestService.updateApprovalOut(outbound_id);
            if(update){
                boolean result2=requestService.outboundDetailStatus(outbound_detail_id,approval_status,reason);
                if(result2){
                    OutboundDto d=requestService.selectOutboundId(outbound_id);
                    if(!"rejected".equals(d.getApproval_status())){
                        boolean result3=requestService.outboundStatus(outbound_id,approval_status);
                        if(result3){
                            r.addFlashAttribute("status","success");
                            r.addFlashAttribute("msg","출고 요청 반려 성공!");
                            return "redirect:/main";
                        }else {
                            r.addFlashAttribute("status","failure");
                            r.addFlashAttribute("msg","출고 요청 반려 후 상태 변경 실패!");
                            return "redirect:/main";
                        }
                    }else {
                        r.addFlashAttribute("status","success");
                        r.addFlashAttribute("msg","출고 요청 반려 성공!");
                        return "redirect:/main";
                    }
                }else {
                    r.addFlashAttribute("status","failure");
                    r.addFlashAttribute("msg","출고 요청 반려 후 상태 변경 실패!");
                    return "redirect:/main";
                }
            }else {
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","출고 요청 반려 실패!");
                return "redirect:/main";
            }
        }else {
            boolean result=requestService.rejectionOut(approver_id,outbound_id,approval_status);
            if(result){
                boolean result2=requestService.outboundDetailStatus(outbound_detail_id,approval_status,reason);
                if(result2){
                    OutboundDto d=requestService.selectOutboundId(outbound_id);
                    if(!"rejected".equals(d.getApproval_status())){
                        boolean result3=requestService.outboundStatus(outbound_id,approval_status);
                        if(result3){
                            r.addFlashAttribute("status","success");
                            r.addFlashAttribute("msg","출고 요청 반려 성공!");
                            return "redirect:/main";
                        }else {
                            r.addFlashAttribute("status","failure");
                            r.addFlashAttribute("msg","출고 요청 반려 후 상태 변경 실패!");
                            return "redirect:/main";
                        }
                    }else {
                        r.addFlashAttribute("status","success");
                        r.addFlashAttribute("msg","출고 요청 반려 성공!");
                        return "redirect:/main";
                    }
                }else {
                    r.addFlashAttribute("status","failure");
                    r.addFlashAttribute("msg","출고 요청 반려 후 상태 변경 실패!");
                    return "redirect:/main";
                }
            }else {
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","출고 요청 반려 실패!");
                return "redirect:/main";
            }
        }
    }

    @GetMapping("/approval/list")
    public String approvalList(Model model,
                               @RequestParam(name = "pageNum", defaultValue = "1") int pageNum){
        Map<String,Object> map=requestService.approvalAll(pageNum);

        model.addAttribute("list", map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "content/approvalList");

        return "layout";
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
