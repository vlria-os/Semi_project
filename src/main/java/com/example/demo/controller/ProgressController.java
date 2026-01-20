package com.example.demo.controller;

import com.example.demo.service.ProgressService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;

    @GetMapping("/progress")
    public String progressList(Model model, HttpSession session,
                               @RequestParam(name = "keyword", defaultValue = "") String keyword,
                               @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(name = "my", defaultValue = "false") String my){
        Map<String, Object> map=new HashMap<>();
        int webuser_id=(int)session.getAttribute("webuser_id");
        int role_id=(int)session.getAttribute("role_id");

        if(my.equals("true")){
            map=progressService.list_my(pageNum,keyword,webuser_id);
            model.addAttribute("content","content/progress_my");
        }else{
            map=progressService.list(pageNum,keyword);
            model.addAttribute("content","content/progress");
        }

        model.addAttribute("list",map.get("list"));
        model.addAttribute("pageInfo", map.get("pageInfo"));
        model.addAttribute("my", my);
        model.addAttribute("keyword", keyword);

        if(role_id==1){
            model.addAttribute("navFragment","fragment/nav/adminNav");
        }else if(role_id==2){
            model.addAttribute("navFragment","fragment/nav/officeNav");
        }else{
            model.addAttribute("navFragment","fragment/nav/fieldNav");
        }

        return "layout";
    }

    @GetMapping("/progress/out")
    public String progressOutList(Model model, HttpSession session,
                                  @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                  @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(name = "my", defaultValue = "false") String my){
        Map<String, Object> map=new HashMap<>();
        int webuser_id=(int)session.getAttribute("webuser_id");
        int role_id=(int)session.getAttribute("role_id");

        if(my.equals("true")){
            map=progressService.list_outMy(pageNum, keyword,webuser_id);
            model.addAttribute("content","content/progress_outMy");
        }else{
            map=progressService.list_out(pageNum, keyword);
            model.addAttribute("content","content/progress_out");
        }

        model.addAttribute("list",map.get("list"));
        model.addAttribute("pageInfo", map.get("pageInfo"));
        model.addAttribute("keyword", keyword);

        if(role_id==1){
            model.addAttribute("navFragment","fragment/nav/adminNav");
        }else if(role_id==2){
            model.addAttribute("navFragment","fragment/nav/officeNav");
        }else{
            model.addAttribute("navFragment","fragment/nav/fieldNav");
        }

        return "layout";
    }
}
