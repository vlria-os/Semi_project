package com.example.demo.controller;


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
    private final int pagesize=6; //한 페이지에 6개씩 보여줄 데이터 수

    @GetMapping("/list")
    public String list(Model model,
            @RequestParam(defaultValue = "1") int pagenum,
            @RequestParam(required = false)String keyword,
            @RequestParam(required = false)String status
            ) {

        int offset=(pagenum -1) * pagesize;
        //1. 총 데이터 수
        int totalCount=service.count(keyword,status);
        //2. 총 페이지 수
        int totalPage=(int)Math.ceil((double) totalCount/pagesize);
        // 3. 페이지별 데이터 가져오기
          List<ProductStockDto> p = service.selectAll(keyword,status,offset,pagesize);

          //4. Model에 담기
          model.addAttribute("p",p);
          model.addAttribute("pagenum",pagenum);
          model.addAttribute("totalPage",totalPage);
          model.addAttribute("status",status);
          model.addAttribute("param",new Object(){
              public String getkeyword() {
                  return keyword;
              }
          });
            return "list";
        }
    }

