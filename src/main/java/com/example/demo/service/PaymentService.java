package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.SettlementDto;
import com.example.demo.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentMapper paymentMapper;
    private final String secretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

    public List<PaymentDto> list(){
        return paymentMapper.select_list();
    }

    public int update(int settlement_id, int webuser_id){
        SettlementDto settlementDto=new SettlementDto();
        settlementDto.setPayment_id(webuser_id);
        settlementDto.setSettlement_id(settlement_id);
        return paymentMapper.update(settlementDto);
    }

    public String createPaymentLink(int amount, String orderName) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // JSON body
            Map<String, Object> body = new HashMap<>();
            body.put("orderName", orderName);
            body.put("amount", amount);
            body.put("successUrl", "https://example.com/success");
            body.put("failUrl", "https://example.com/fail");

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String base64Key = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
            headers.set("Authorization", "Basic " + base64Key);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // Sandbox v2 API 엔드포인트
            String url = "https://sandbox.tosspayments.com/v2/payment-links";

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || responseBody.get("url") == null) {
                throw new RuntimeException("Toss API 응답에 url이 없습니다: " + responseBody);
            }

            return (String) responseBody.get("url");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Toss API 호출 실패: " + e.getMessage());
        }
    }
}
