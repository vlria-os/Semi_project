package com.example.demo.controller;

import com.example.demo.dto.Inbound_detailDto;
import com.example.demo.service.InboundService;
import com.example.demo.service.RequestService;
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
public class inbound_app_Controller {
    private final InboundService inboundService;
    private final RequestService requestService;


    @GetMapping("/admin/inbound_app")
    public String main(HttpSession session, Model model,
                       @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
        Integer role_id = (Integer) session.getAttribute("role_id");
        Integer webuser_id = (Integer) session.getAttribute("webuser_id");

        if (role_id == null) {
            return "redirect:/login";
        }else if(role_id == 1){
            Map<String,Object> map=requestService.adminBound(pageNum);

            model.addAttribute("list",map.get("list"));
            model.addAttribute("pageInfo",map.get("pageInfo"));
        }else {
            if (webuser_id == null) {
                return "redirect:/login";
            }

            Map<String,Object> map=requestService.userBound(pageNum,webuser_id);

            model.addAttribute("list",map.get("list"));
            model.addAttribute("pageInfo",map.get("pageInfo"));
        }
        model.addAttribute("webuser_id",webuser_id);
        model.addAttribute("role_id",role_id);
        return "/admin/inbound_app";
    }

//    @GetMapping("/goinbound")
//    @ResponseBody
//    public Map<String,Object> goinbound(HttpSession session,
//                                        Model model,
//                                        @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
//        int role_id = (int) session.getAttribute("role_id");
//        int webuser_id = (int) session.getAttribute("webuser_id");
//        if(role_id == 1){
//            Map<String,Object> map=requestService.inboundAll(pageNum);
//            return map;
//        }else {
//            Map<String,Object> map=requestService.selectInbound(pageNum,webuser_id);
//            return map;
//        }
//
//    }
//
//    @GetMapping("/gooutbound")
//    @ResponseBody
//    public Map<String,Object> gooutbound(HttpSession session,
//                                         @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
//        int role_id = (int) session.getAttribute("role_id");
//        int webuser_id = (int) session.getAttribute("webuser_id");
//        if(role_id == 1){
//            Map<String,Object> map=requestService.outboundAll(pageNum);
//            return map;
//        }else {
//            Map<String,Object> map=requestService.selectOutbound(pageNum,webuser_id);
//            return map;
//        }
//    }

    @PostMapping("/admin/inbound_app")
    @ResponseBody
    public Map<String,Object> inbound_app(int inbound_id,
                              Inbound_detailDto inbound_detailDto,
                              String reason,
                              HttpSession session){

        int approver_id=(int)session.getAttribute("webuser_id");

        int n=inboundService.update_approval(inbound_id,inbound_detailDto,approver_id);
        Map<String,Object> map=requestService.inboundList(inbound_id);
        List<Inbound_detailDto> detailList=(List<Inbound_detailDto>)map.get("list");
        Map<Integer,String> names=(Map<Integer,String>)map.get("names");

        Map<String,Object> result = new HashMap<>();
        result.put("success", n>0);
        result.put("detailList", detailList); // 최신 상세 리스트 반환
        result.put("names",names);
        return result;
    }
}
