package com.example.demo.controller;

import com.example.demo.dto.ListDto;
import com.example.demo.dto.ProductStockDto;
import com.example.demo.service.ProductStockService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor //@Autowired 대신 사용
public class StockListController {
    private final ProductStockService stockService;
    private final int pagesize=6;

    @GetMapping("/stockList")
    public String list(
            @RequestParam(defaultValue = "1") int pagenum,
            @RequestParam(required = false)String keyword,
            @RequestParam(required = false)String status,
            Model model,
            HttpSession session) {

        int offset=(pagenum -1) * pagesize;
        //1. 총 데이터 수
        int totalCount=stockService.count(keyword,status);
        //2. 총 페이지 수
        int totalPage=(int)Math.ceil((double) totalCount/pagesize);
        // 3. 페이지별 데이터 가져오기
        List<ProductStockDto> p = stockService.selectAll(keyword,status,offset,pagesize);

        //4. Model에 담기
        model.addAttribute("p",p);
        model.addAttribute("pagenum",pagenum);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("status",status);
        model.addAttribute("keyword",keyword);
        model.addAttribute("content", "content/stockList");

        if ((int) session.getAttribute("role_id") == 1) {
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            return "layout";
        } else if ((int) session.getAttribute("role_id") == 2) {
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            return "layout";
        } else if ((int) session.getAttribute("role_id") == 3) {
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            return "layout";
        }
        return "layout";
    }

    @PostMapping("/stock/update")
    public String updateStockQuantity(
            @RequestParam Long productId,
            @RequestParam int quantity) {
        if (productId != null ) {
            stockService.updateQuantity(productId, quantity);
        }
        return "redirect:/list";
    }
}
