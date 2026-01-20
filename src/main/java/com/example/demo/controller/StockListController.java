package com.example.demo.controller;

import com.example.demo.dto.ListDto;
import com.example.demo.dto.ProductStockDto;
import com.example.demo.dto.StockQuantityDto;
import com.example.demo.service.ProductStockService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor //@Autowired 대신 사용
public class StockListController {
    private final ProductStockService stockService;

    @GetMapping("/stockList")
    public String list(Model model, HttpSession session,
                       @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "status", required = false) String status) {
        Map<String,Object> map=stockService.selectAll(pageNum, keyword, status);

        model.addAttribute("p",map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("content", "content/stockList");
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);

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

    @GetMapping("/stockList/req")
    public String list_req(Model model, HttpSession session,
                       @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "status", required = false) String status) {
        Map<String,Object> map=stockService.selectAll(pageNum, keyword, status);

        model.addAttribute("p",map.get("list"));
        model.addAttribute("pageInfo",map.get("pageInfo"));
        model.addAttribute("content", "content/outbound_req");

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

    @GetMapping("/stock/updateForm")
    @ResponseBody
    public List<StockQuantityDto> updateStockQuantityForm(@RequestParam int productId) {
        List<StockQuantityDto> list=stockService.stockQuantityList(productId);
        return list;
    }

    @PostMapping("/stock/update")
    public String updateQuantity(StockQuantityDto dto){
        stockService.stockQuantityUpdate(dto.getQuantity(), dto.getStockId(), dto.getLotInId());
        return "redirect:/stockList";
    }
}
