package com.example.demo.controller;

import com.example.demo.dto.SalesStatsDto;
import com.example.demo.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statsService;

    @GetMapping("/statistics")
    public String itemStats(Model model) throws Exception{
        Map<String, Object> statsData= statsService.getSalesBoard();
        model.addAttribute("stats", statsData.get("summary"));
        model.addAttribute("rankList", statsData.get("ranking"));

//        ObjectMapper mapper=new ObjectMapper();
//        String Ranking=mapper.writeValueAsString(statsData.get("ranking"));
//        model.addAttribute("Ranking", Ranking);

        return "stats/statistics";
    }

    @GetMapping("/period")
    public String periodStats(Model model){
        List<SalesStatsDto> periodData= statsService.getPeriodStats();
        long totalRev=0;
        long totalQty=0;
        for(SalesStatsDto dto : periodData){
            totalRev += dto.getRevenue();
            totalQty += dto.getQuantity();
        }
        model.addAttribute("periodList", periodData);
        model.addAttribute("totalRev", totalRev);
        model.addAttribute("totalQty", totalQty);
        model.addAttribute("avgPrice", totalQty > 0 ? totalRev/totalQty : 0);
        return "stats/period";
    }

    @GetMapping("/waste")
    public String wasteStats(Model model){
        List<SalesStatsDto> wasteData=statsService.getWasteStats();
        long totalWaste= wasteData.stream().mapToLong(SalesStatsDto::getWaste_quantity).sum();
        long totalOut= wasteData.stream().mapToLong(SalesStatsDto::getTotal_quantity).sum();
        double avgWasteRate=totalOut > 0 ? (double) totalWaste / totalOut * 100 : 0;

        model.addAttribute("wasteList", wasteData);
        model.addAttribute("avgWasteRate", Math.round(avgWasteRate * 10)/ 10.0);
        return "stats/waste";
    }
}
