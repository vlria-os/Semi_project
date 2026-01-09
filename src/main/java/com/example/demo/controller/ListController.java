package com.example.demo.controller;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ListDto;
import com.example.demo.dto.ProductStockDto;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor //@Autowired 대신 사용
public class ListController {
    private final ProductStockService service;
  //  private final CategoryMapper categoryMapper;

    @GetMapping("/list")
    public String list(
            @RequestParam(required = false)String keyword,
            @RequestParam(required = false)String product_type,
            Model model) {
        System.out.println("keyword==>" + keyword);
          List<ProductStockDto> p = service.selectAll(keyword,product_type);
          model.addAttribute("p",p);

    //      List<CategoryDto> categories=categoryMapper.selectAllCategories();
      //    model.addAttribute("categories",categories);
          return "list";
        }
}

