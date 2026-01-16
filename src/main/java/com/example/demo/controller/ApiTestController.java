package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class ApiTestController {

    @GetMapping("/page")
    public String page(){
        return "testApi";
    }

    @GetMapping("/test")
    @ResponseBody
    public String getCulture(){
        String url="https://apis.data.go.kr/6450000/JeonbukPrincipalProductService/getJeonbukPrincipalProduct" +
                "?ServiceKey=14828702d32a2c63fa843d3cdab151ae271fa46bd7f7284daebd0555ab349fd2&Category=B&Area=01";
        /*
        RestTemplate: Spring에서 제공하는 HTTP 통신 도구
        GET / POST / PUT / DELETE 등 REST API 호출 가능
         */
        RestTemplate restTemplate=new RestTemplate();
        String data=restTemplate.getForObject(url,String.class);
        System.out.println(data);
        return data;
    }
}
