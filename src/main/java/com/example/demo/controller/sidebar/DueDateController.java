package com.example.demo.controller.sidebar;

import com.example.demo.dto.SearchDto;
import com.example.demo.service.DueDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DueDateController {
    private final DueDateService dService;
    @GetMapping("/fragment/sidebar/dueDate")
    public String dueDate(SearchDto searchDto,
                          @RequestParam(value = "viewType", defaultValue = "soon") String viewType,
                          @RequestParam(value="pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "typeFilter", defaultValue = "all") String typeFilter,
                          @RequestParam(value = "warehouseFilter", required = false) String warehouseFilter,
                          @RequestParam(value = "isAjax", defaultValue = "false") boolean isAjax,
                          Model model){

        String keyword= searchDto.getKeyword();
        Map<String, Object> result= dService.getDueDateList(pageNum, typeFilter, warehouseFilter, viewType, keyword);
        searchDto.setKeyword(keyword);

        //페이지로 다시 이동 후 "전체"에 자동 선택
        if(searchDto.getField() == null || searchDto.getField().isEmpty()){
            searchDto.setField("all");
        }
//        boolean lot_status=
        model.addAttribute("viewType", viewType);
        model.addAttribute("list", result.get("list"));
        model.addAttribute("pageInfo", result.get("pageInfo"));
        model.addAttribute("warehouseList", dService.getWarehouse());
        model.addAttribute("typeFilter", typeFilter);
        model.addAttribute("warehouseFilter", warehouseFilter);
        model.addAttribute("searchDto", searchDto);
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "fragment/sidebar/dueDate");

        if(isAjax){return "fragment/sidebar/dueDate :: #dueDateContainer";}
        return "layout";
    }
}
