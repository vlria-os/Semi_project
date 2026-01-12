package com.example.demo.controller;

import com.example.demo.dto.SearchDto;
import com.example.demo.dto.WarehouseDto;
import com.example.demo.service.WarehouseService;
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
    public String warehouselist(Model model, SearchDto dto, @RequestParam(value="pageNum", defaultValue = "1") int pageNum, @RequestParam(value="isAjax", defaultValue = "false") boolean isAjax){
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
        Map<String, Object> result=service.selectWarehouseList(pageNum, dto);
        model.addAttribute("list", result.get("list"));
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("searchDto", dto);

        if(isAjax){return "warehouse/warehouse-list :: #warehouse-table";}

        return "warehouse/warehouse-list";
    }

    @GetMapping("/warehouse/insert")
    public String warehouseInsertForm(Model model){
        model.addAttribute("warehouseDto", new WarehouseDto());
        return "warehouse/warehouse-insert";
    }
    @PostMapping("/warehouse/insert")
    public String warehouseInsert(WarehouseDto warehouseDto){
        warehouseDto.setIs_active("OPEN");
        service.insertWarehouse(warehouseDto);
        return "redirect:/warehouse/list";
    }
    @GetMapping("/warehouse/update")
    public String warehouseUpdateForm(@RequestParam("warehouse_id") int warehouse_id, Model model){
        WarehouseDto dto=service.selectAll(warehouse_id);
        model.addAttribute("warehouseDto", dto);
        return "warehouse/warehouse-update";
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
