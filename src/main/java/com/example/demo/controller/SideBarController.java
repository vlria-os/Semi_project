package com.example.demo.controller;

import com.example.demo.service.DueDateService;
import com.example.demo.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SideBarController {
    private final DueDateService dService;
    private final WarehouseService wService;

    @GetMapping("/main/sidebar")
    public String getSideBar(Model model){
        model.addAttribute("dueList", dService.getSideDateList());
        model.addAttribute("limitList", wService.getSideWarehouseList());
        return "fragments/sidebar :: sidebarContent";
    }
}
