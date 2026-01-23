package com.example.demo.controller;

import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.SettlementDto;
import com.example.demo.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

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
        return "redirect:/payment";
    }

    @GetMapping("/payment/guest/{settlementId}")
    public String generateLink(Model model,
                               @PathVariable("settlementId") int settlementId) {
        PaymentDto paymentDto =paymentService.list_one(settlementId);
        model.addAttribute("item", paymentDto);
        return "content/payment_guest"; // 위 HTML 페이지 이름
    }
}
