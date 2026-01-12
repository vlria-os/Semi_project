package com.example.demo.controller;

import com.example.demo.dto.SearchDto;
import com.example.demo.dto.WarehouseDto;
import com.example.demo.service.WarehouseService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService service;

    @GetMapping("/warehouse/list")
    public String warehouselist(HttpSession session, Model model, SearchDto dto, @RequestParam(value="pageNum", defaultValue = "1") int pageNum, @RequestParam(value="isAjax", defaultValue = "false") boolean isAjax){
        //Search Bar 값 없을때 오류 방지
        if(dto==null){
            dto = new SearchDto();
            dto.setField("all");
            dto.setKeyword("");
        }
        //페이지로 다시 이동 후 "전체"에 자동 선택
        if(dto.getField() == null || dto.getField().isEmpty()){
            dto.setField("all");
        }

        if((int)session.getAttribute("role_id")!=1) {
            return "redirect:/";
        }
        Map<String, Object> result = service.selectWarehouseList(pageNum, dto);
        model.addAttribute("list", result.get("list"));
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("searchDto", dto);
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "warehouse/warehouse-list");

        if (isAjax) {
            return "warehouse/warehouse-list :: #warehouse-table";
        }
        return "layout";
    }

    @GetMapping("/warehouse/insert")
    public String warehouseInsertForm(HttpSession session, Model model){
        model.addAttribute("warehouseDto", new WarehouseDto());
        if((int)session.getAttribute("role_id")!=1) {
            return "redirect:/";
        }
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "warehouse/warehouse-insert");
        return "layout";
    }
    @PostMapping("/warehouse/insert")
    public String warehouseInsert(WarehouseDto warehouseDto){
        warehouseDto.setIs_active("OPEN");
        service.insertWarehouse(warehouseDto);
        return "redirect:/warehouse/list";
    }
    @GetMapping("/warehouse/update")
    public String warehouseUpdateForm(@RequestParam("warehouse_id") int warehouse_id, HttpSession session, Model model){
        WarehouseDto dto=service.selectAll(warehouse_id);
        if((int)session.getAttribute("role_id")!=1) {
            return "redirect:/";
        }
        model.addAttribute("warehouseDto", dto);
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "warehouse/warehouse-update");
        return "layout";
    }
    @PostMapping("/warehouse/update")
    public String warehouseUpdate(WarehouseDto dto){
        service.updateWarehouse(dto);
        return "redirect:/warehouse/list";
    }
    @PostMapping("/warehouse/delete")
    public String warehouseDelete(@RequestParam("warehouse_id") int warehouse_id){
        service.deleteWarehouse(warehouse_id);
        return "redirect:/warehouse/list";
    }
}
