package com.example.demo.controller;

import com.example.demo.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Controller
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statsService;

    @GetMapping("/statistics")
    public String itemStats(Model model){
        Map<String, Object> statsData= statsService.getSalesBoard();
        model.addAttribute("stats", statsData.get("summary"));
        model.addAttribute("rankList", statsData.get("ranking"));

        ObjectMapper mapper=new ObjectMapper();
        String Ranking=mapper.writeValueAsString(statsData.get("ranking"));
        model.addAttribute("Ranking", Ranking);

        return "stats/statistics";
    }
}
