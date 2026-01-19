package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class ApiAdsController {

    @GetMapping("/page")
    public String page(){
        return "testApi";
    }

    @GetMapping("/jeonbuk")
    @ResponseBody
    public ResponseEntity<String> getJeonbuk(){
        String base = "https://api.odcloud.kr/api/15050021/v1/uddi:7b6e515f-d95d-4415-92a1-93ef5b6c72c8";

        String url = UriComponentsBuilder
                .fromUriString(base)
                // ✅ odcloud는 보통 serviceKey (S 대문자 ServiceKey 아니고)
                .queryParam("serviceKey", "14828702d32a2c63fa843d3cdab151ae271fa46bd7f7284daebd0555ab349fd2")
                .queryParam("page", 1)
                .queryParam("perPage", 10)
                .build(true)
                .toUriString();

        RestTemplate rt = new RestTemplate();

        try {
            String body = rt.getForObject(url, String.class);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(body);
        } catch (HttpStatusCodeException e) {
            // ✅ 외부 API가 준 에러(JSON/텍스트)를 그대로 확인 가능
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/haenam")
    @ResponseBody
    public ResponseEntity<String> getHaenam() {

        String base = "https://api.odcloud.kr/api/3037894/v1/uddi:cb64a5a9-6291-4b7a-8032-c9fca056f37c";

        String url = UriComponentsBuilder
                .fromUriString(base)
                // ✅ odcloud는 보통 serviceKey (S 대문자 ServiceKey 아니고)
                .queryParam("serviceKey", "14828702d32a2c63fa843d3cdab151ae271fa46bd7f7284daebd0555ab349fd2")
                .queryParam("page", 1)
                .queryParam("perPage", 10)
                .build(true)
                .toUriString();

        RestTemplate rt = new RestTemplate();

        try {
            String body = rt.getForObject(url, String.class);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(body);
        } catch (HttpStatusCodeException e) {
            // ✅ 외부 API가 준 에러(JSON/텍스트)를 그대로 확인 가능
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

}
