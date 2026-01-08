package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductListController {
    private final ProductService productService;

    @GetMapping("/common/product_list")
    public String inbound_reqForm(Model model){
        List<ProductDto> productDtos=productService.productList("");
        model.addAttribute("list", productDtos);
        return "common/product_list";
    }

    @PostMapping("/common/product_list")
    public String product_search(String keyword,
                                 Model model){
        List<ProductDto> productDtos=productService.productList(keyword);
        model.addAttribute("list", productDtos);
        model.addAttribute("keyword",keyword);
        return "common/product_list";
    }

    @GetMapping("/common/product_search")
    @ResponseBody
    public List<ProductDto> productSearch(@RequestParam String keyword) {
        System.out.println("키워드===>"+keyword);
        System.out.println(productService.productList(keyword));
        return productService.productList(keyword);
    }

    @GetMapping("/common/product_insert")
    public String product_insertForm(Model model){
        model.addAttribute("productDto",new ProductDto());
        return "common/product_insert";
    }

    @PostMapping("/common/product_insert")
    public String product_insert(ProductDto productDto){
        int n=productService.insert(productDto);
        return "redirect:/common/product_list";
    }

}
