package com.example.demo.controller;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/content/category_insert")
    public String categoryForm(Model model){
        List<CategoryDto> rootCategory = productService.getRootCategories();

        model.addAttribute("rootCategory", rootCategory);
        model.addAttribute("categoryDto", new CategoryDto());
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "content/category_insert");
        return "layout";
    }

    @PostMapping("/content/category_insert")
    public String category_insert(CategoryDto categoryDto){
        int n=categoryService.insert(categoryDto);
        return "redirect:/content/productList";
    }
}
