package com.example.demo.controller;

import com.example.demo.dto.SearchDto;
import com.example.demo.service.ExpiredService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ExpiredController {
    private final ExpiredService eService;

    @GetMapping("/sidebar/expiredList")
    public String expiredList(SearchDto searchDto, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                              @RequestParam(value = "typeFilter", defaultValue = "all") String typeFilter,
                              @RequestParam(value = "warehouseFilter", required = false) String warehouseFilter,
                              @RequestParam(value = "isAjax", defaultValue = "false") boolean isAjax,
                              Model model){
        Map<String, Object> result=eService.getExpiredList(pageNum, typeFilter, warehouseFilter, searchDto.getKeyword());
        model.addAttribute("list", result.get("list"));
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("warehouseList", eService.getWarehouse());
        model.addAttribute("typeFilter", typeFilter);
        model.addAttribute("warehouseFilter", warehouseFilter);
        model.addAttribute("searchDto", searchDto);

        if(isAjax){
            return "sidebar/expiredList :: #expiredListContainer";
        }
        return "sidebar/expiredList";
    }
}
