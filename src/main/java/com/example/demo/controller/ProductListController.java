package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.Product_imageDto;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProductListController {
    private final String UPLOADPATH="c:/image_Semi/";
    private final ProductService productService;

    @GetMapping("content/productList")
    public String productList(Model model,
                              HttpSession session) {
        model.addAttribute("list", productService.productList());
        if((int)session.getAttribute("role_id")==1){
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/productList");
        }else if((int)session.getAttribute("role_id")==2){
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "content/productList");
        }else if((int)session.getAttribute("role_id")==3){
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "content/productList");
        }

        return "layout";
    }

    @GetMapping("/common/product_list")
    public String inbound_reqForm(Model model){
        List<ProductDto> productDtos=productService.productList("");
        model.addAttribute("list", productDtos);
        return "common/product_list";
    }

    @PostMapping("/common/product_list")
    public String product_search(String keyword,
                                 Model model){
        List<ProductDto> productDtos=productService.productList(keyword);
        model.addAttribute("list", productDtos);
        model.addAttribute("keyword",keyword);
        return "common/product_list";
    }

    @GetMapping("/common/product_search")
    @ResponseBody
    public List<ProductDto> productSearch(@RequestParam String keyword) {
        System.out.println(productService.productList(keyword));
        return productService.productList(keyword);
    }

    @GetMapping("/common/product_insert")
    public String product_insertForm(Model model){
        model.addAttribute("productDto",new ProductDto());
        return "common/product_insert";
    }

    @PostMapping("/common/product_insert")
    public String product_insert(ProductDto productDto){
        MultipartFile file=productDto.getFile();

        String orgFileName = file.getOriginalFilename();
        String extName = orgFileName.substring(orgFileName.lastIndexOf("."));
        String saveFileName = UUID.randomUUID() + extName;
        try {
            File f = new File(UPLOADPATH + saveFileName);
            file.transferTo(f);
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }

        Product_imageDto imageDto=new Product_imageDto(0,UPLOADPATH + saveFileName,saveFileName);

        int n=productService.insert(productDto, imageDto);
        return "redirect:/common/product_list";
    }

}
