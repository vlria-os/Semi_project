package com.example.demo.controller;

import com.example.demo.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;

    @GetMapping("/progress")
    public String progressList(Model model){
        model.addAttribute("list",progressService.list());
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "content/progress");
        return "layout";
    }

    @GetMapping("/progress/out")
    public String progressOutList(){

        return "layout";
    }
}
