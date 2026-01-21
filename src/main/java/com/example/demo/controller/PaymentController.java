package com.example.demo.controller;

import com.example.demo.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final String secretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

    @GetMapping("/payment")
    public String paymentPage(Model model) {
        model.addAttribute("list", paymentService.list());
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "content/payment");
        return "layout"; // 위 HTML 페이지 이름
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(HttpSession session,
                                 @RequestParam(name = "settlementId") int settlement_id) {
        System.out.println(settlement_id);
        paymentService.update(settlement_id, (int)session.getAttribute("webuser_id"));
        return "redirect:/payment";
    }

    @GetMapping("/payment/fail")
    @ResponseBody
    public String paymentFail(@RequestParam Map<String, String> params) {
        return "결제 실패! " + params.toString();
    }

    @PostMapping("/generate-link")
    @ResponseBody
    public Map<String, String> generateLink(@RequestParam int amount) {
        String paymentId = paymentService.createPaymentLink(amount, "월말 정산");

        Map<String, String> result = new HashMap<>();
        result.put("url", "https://toss.im/pay/" + paymentId);
        return result;
    }
}
