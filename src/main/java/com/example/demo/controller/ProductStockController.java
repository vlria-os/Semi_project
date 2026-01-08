//package com.example.demo.controller;
//
//import com.example.demo.dto.ProductStockDto;
//import com.example.demo.service.ProductStockService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class ProductStockController {
//    private final ProductStockService service;
//
//    @GetMapping("/product/list")
//    public String productlist(Model model) {
//
//        List<ProductStockDto> p = service.selectAll();
//        model.addAttribute("p", p);
//        return "list";
//    }
//}
