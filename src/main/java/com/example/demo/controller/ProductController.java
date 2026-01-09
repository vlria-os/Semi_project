package com.example.demo.controller;

import com.example.demo.dto.ProductListDto;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public String list(Model model) {
        model.addAttribute("products",productService.getAllProducts());
        return "product/list";
    }
    @GetMapping("/new")
    public  String form() {
        return "product/form";
    }
    @PostMapping
    public String save (ProductListDto productListDto) {
        return "redirect:/product";

    }
}

