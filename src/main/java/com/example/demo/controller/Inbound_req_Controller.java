package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.InboundService;
import com.example.demo.service.ProductService;
import com.example.demo.service.WarehouseService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class Inbound_req_Controller {
    private final InboundService inboundService;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    @GetMapping("/office_staff/inbound_req")
    public String inbound_reqForm(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, Model model,
                                  @ModelAttribute SearchDto searchDto){
        Map<String,Object> map=productService.productList(pageNum, searchDto);

        model.addAttribute("list", map.get("productDtos"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("navFragment", "fragment/nav/officeNav");
        model.addAttribute("content", "content/inbound_req");
        model.addAttribute("searchDto", searchDto);
        return "layout";
    }

    @PostMapping("/office_staff/inbound_req")
    @ResponseBody
    public Map<String, Object> inbound_req(@RequestBody List<Inbound_reqDto> list,
                              HttpSession session){
        int webuser_id = (int) session.getAttribute("webuser_id");
        int n=inboundService.insert_request(list, webuser_id);

        if(n>0){
            return Map.of("success", true);
        }
        return Map.of("success", false);
    }

    @GetMapping("/office_staff/inbound_req/warehouse")
    @ResponseBody
    public List<WarehouseDto> warehouseList(String status){
        return warehouseService.warehouseType(status);
    }
}