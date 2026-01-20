package com.example.demo.service;

import com.example.demo.dto.SalesStatsDto;
import com.example.demo.mapper.StatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsMapper statsMapper;

    public Map<String, Object> getSalesBoard(){
        Map<String, Object> data=new HashMap<>();
        SalesStatsDto summary=statsMapper.selectSalesSummary();
        data.put("summary", summary);

        List<SalesStatsDto> ranking= statsMapper.selectProductRanking();
        if(summary.getTotal_revenue() >0){
            ranking.forEach(item ->{
                double share=(double) item.getRevenue()/summary.getTotal_revenue() *100;
                item.setShare(Math.round(share *10) / 10.0);
            });
        }
        data.put("ranking", ranking);
        return data;
    }

    public List<SalesStatsDto> getPeriodStats(){return statsMapper.selectPeriodSales();}
    public List<SalesStatsDto> getWasteStats(){return statsMapper.selectWasteStats();}
}
