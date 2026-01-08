package com.example.demo.controller;

import com.example.demo.dto.Inbound_reqDto;
import com.example.demo.dto.Inbound_reqListDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.service.InboundService;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class Inbound_req_Controller {
    private final InboundService inboundService;
    private final ProductService productService;

    @GetMapping("/office_staff/inbound_req")
    public String inbound_reqForm(Model model){
        List<ProductDto> productDtos=productService.productList("");
        model.addAttribute("list", productDtos);
        return "office_staff/inbound_req";
    }

    @PostMapping("/office_staff/inbound_req")
    @ResponseBody
    public Map<String, Object> inbound_req(@RequestBody List<Map<String, String>> data,
                              HttpSession session,
                              Model model){
        List<Inbound_reqDto> inbound_reqDtos=new ArrayList<>();
        //int webuser_id=(int)session.getAttribute("webuser_id");
        int webuser_id=1000;

        for (Map<String, String> item : data) {
            Inbound_reqDto inbound_reqDto=new Inbound_reqDto(
                    Integer.parseInt(item.get("product_id")),
                    1,
                    //Integer.parseInt(item.get("warehouse")),
                    Integer.parseInt(item.get("qty"))
            );
            inbound_reqDtos.add(inbound_reqDto);
        }
        int n=inboundService.insert_request(inbound_reqDtos, webuser_id);
        return Map.of("status", "success", "count", data.size());
    }
}

/*
<input name="inbound_reqDtos[0].product_id" />
<input name="inbound_reqDtos[0].warehouse_id" />
<input name="inbound_reqDtos[0].quantity" />

<input name="inbound_reqDtos[1].product_id" />
<input name="inbound_reqDtos[1].warehouse_id" />
<input name="inbound_reqDtos[1].quantity" />

<input name="inbound_reqDtos[2].product_id" />
<input name="inbound_reqDtos[2].warehouse_id" />
<input name="inbound_reqDtos[2].quantity" />
 */