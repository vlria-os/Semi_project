package com.example.demo.controller;

import com.example.demo.dto.ListDto;
import com.example.demo.dto.ProductStockDto;
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

    @GetMapping("/list")
    public String list(
            @RequestParam(required = false)String product_name,
            @RequestParam(required = false)Integer category_id,
            @RequestParam(required = false)String category_name,
            @RequestParam(required = false)String product_type,
            Model model) {

        //재고목록
        List<ListDto> m = service.selectlist();
        model.addAttribute("m", m);

//        List<ProductStockDto> p = service.selectAll();
//        model.addAttribute("p", p);
//        return "list";


        List<ProductStockDto> p = service.selectAll(product_name,category_id,category_name,product_type);
          model.addAttribute("p",p);
          return "list";
        }
}
