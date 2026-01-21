package com.example.demo.controller;

import com.example.demo.dto.SalesStatsDto;
import com.example.demo.service.StatisticsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statsService;

    @GetMapping("/stats/statistics")
    public String itemStats(Model model, HttpSession session) throws Exception{
        Map<String, Object> statsData= statsService.getSalesBoard();
        model.addAttribute("stats", statsData.get("summary"));
        model.addAttribute("rankList", statsData.get("ranking"));
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "fragment/stats/statistics");

        if((int)session.getAttribute("role_id")!=1) {
            return "redirect:/";
        }
//        ObjectMapper mapper=new ObjectMapper();
//        String Ranking=mapper.writeValueAsString(statsData.get("ranking"));
//        model.addAttribute("Ranking", Ranking);

        return "layout";
    }

    @GetMapping("/stats/period")
    public String periodStats(Model model, HttpSession session){
        List<SalesStatsDto> periodData= statsService.getPeriodStats();
        long totalRev=0;
        long totalQty=0;
        for(SalesStatsDto dto : periodData){
            totalRev += dto.getRevenue();
            totalQty += dto.getQuantity();
        }
        if((int)session.getAttribute("role_id")!=1) {
            return "redirect:/";
        }
        model.addAttribute("periodList", periodData);
        model.addAttribute("totalRev", totalRev);
        model.addAttribute("totalQty", totalQty);
        model.addAttribute("avgPrice", totalQty > 0 ? totalRev/totalQty : 0);
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "fragment/stats/period");
        return "layout";
    }

    @GetMapping("/stats/waste")
    public String wasteStats(Model model, HttpSession session){
        List<SalesStatsDto> wasteData=statsService.getWasteStats();
        long totalWaste= wasteData.stream().mapToLong(SalesStatsDto::getWaste_quantity).sum();
        long totalOut= wasteData.stream().mapToLong(SalesStatsDto::getTotal_quantity).sum();
        double avgWasteRate=totalOut > 0 ? (double) totalWaste / totalOut * 100 : 0;

        if((int)session.getAttribute("role_id")!=1) {
            return "redirect:/";
        }

        model.addAttribute("wasteList", wasteData);
        model.addAttribute("totalWaste", totalWaste);
        model.addAttribute("totalQty", totalOut);
        model.addAttribute("avgWasteRate", Math.round(avgWasteRate * 10)/ 10.0);
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "fragment/stats/waste");
        return "layout";
    }
}
