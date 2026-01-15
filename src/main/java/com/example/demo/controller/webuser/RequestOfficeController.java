package com.example.demo.controller.webuser;

import com.example.demo.service.RequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RequestOfficeController {
    private final RequestService requestService;

    @GetMapping("/content/requestList/office")
    public String RequestList(HttpSession session,
                              Model model,
                              @RequestParam(name = "pageNum",defaultValue = "1") int pageNum){
        int webuser_id=(int)session.getAttribute("webuser_id");
        Map<String,Object> map=requestService.selectInbound(pageNum,webuser_id);

        model.addAttribute("list",map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("navFragment", "fragment/nav/officeNav");
        model.addAttribute("content", "content/requestList_office");

        System.out.println(map.get("list"));

        return "layout";
    }

}
