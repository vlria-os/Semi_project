package com.example.demo.controller;

import com.example.demo.service.DueDateService;
import com.example.demo.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalSidebarAdvice {
    private final DueDateService dService;
    private final WarehouseService wService;

    @ModelAttribute
    public void addSidebar(Model model){
        model.addAttribute("dueList", dService.getSideDateList());
        model.addAttribute("limitList", wService.getSideWarehouseList());
    }
}
