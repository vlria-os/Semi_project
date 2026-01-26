package com.example.demo.controller;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.Product_imageDto;
import com.example.demo.dto.SearchDto;
import com.example.demo.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProductListController {
    private final String UPLOADPATH = "c:/image_Semi/";
    private final ProductService productService;

    @GetMapping("/content/productList")
    public String productList(Model model,
                              @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                              HttpSession session,
                              SearchDto searchDto,
                              @RequestParam(name = "isAjax", defaultValue = "false") boolean isAjax) {
        Map<String, Object> map = productService.productList(pageNum, searchDto);

        //Search Bar 값 없을때 오류 방지
        if(searchDto==null){
            searchDto = new SearchDto();
            searchDto.setField("all");
            searchDto.setKeyword("");
        }
        //페이지로 다시 이동 후 "전체"에 자동 선택
        if(searchDto.getField() == null || searchDto.getField().isEmpty()){
            searchDto.setField("all");
        }

        model.addAttribute("list", map.get("productDtos"));
        model.addAttribute("pageInfo", map.get("pageInfo"));
        model.addAttribute("searchDto", searchDto);

        if(isAjax){ return "content/productList :: #listContainer";}

        Integer roleId = (Integer) session.getAttribute("role_id");
        String nav= switch (roleId != null ? roleId : 1){
            case 2 -> "officeNav";
            case 3 -> "fieldNav";
            default -> "adminNav";
        };

        model.addAttribute("navFragment", "fragment/nav/" + nav);
        model.addAttribute("content", "content/productList");

        return "layout";
    }

    @GetMapping("/content/product_update")
    public String productUpdateForm(@RequestParam("product_id") int productId, Model model){
        ProductDto productDto= productService.getProductById(productId);
        List<CategoryDto> rootCategory= productService.getRootCategories();

        model.addAttribute("productDto", productDto);
        model.addAttribute("rootCategory", rootCategory);
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "content/product_update");
        return "layout";
    }

    @PostMapping("/content/productUpdate")
    public String productUpdate(ProductDto productDto){
        productService.updateProduct(productDto);
        return "redirect:/content/productList";
    }

    @PostMapping("/content/product_delete")
    public String productDelete(@RequestParam("product_id") int productId){
        productService.deleteProduct(productId);
        return "redirect:/content/productList";
    }

//        if ((int) session.getAttribute("role_id") == 1) {
//            model.addAttribute("navFragment", "fragment/nav/adminNav");
//            model.addAttribute("content", "content/productList");
//        } else if ((int) session.getAttribute("role_id") == 2) {
//            model.addAttribute("navFragment", "fragment/nav/officeNav");
//            model.addAttribute("content", "content/productList");
//        } else if ((int) session.getAttribute("role_id") == 3) {
//            model.addAttribute("navFragment", "fragment/nav/fieldNav");
//            model.addAttribute("content", "content/productList");
//        }


//    @GetMapping("/common/product_list")
//    public String inbound_reqForm(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, Model model) {
//        Map<String, Object> map = productService.productList(pageNum);
//        model.addAttribute("list", map.get("productDtos"));
//        model.addAttribute("pageInfo", map.get("pageInfo"));
//        return "common/product_list";
//    }

//    @PostMapping("/common/product_list")
//    public String product_search(String keyword,
//                                 @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
//                                 Model model) {
//        Map<String, Object> map = productService.productList(pageNum, keyword);
//        model.addAttribute("list", map.get("productDtos"));
//        model.addAttribute("keyword", keyword);
//        return "common/product_list";
//    }

    @GetMapping("/common/product_search")
    @ResponseBody
    public Map<String, Object> productSearch(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                             @ModelAttribute SearchDto searchDto) {
        Map<String, Object> map = productService.productList(pageNum, searchDto);
        System.out.println("=========================>"+map.get("pageInfo"));
        return map;
    }

    @GetMapping("/content/product_insert")
    public String product_insertForm(Model model, ProductDto productDto) {
        List<CategoryDto> rootCategory = productService.getRootCategories();

        model.addAttribute("rootCategory", rootCategory);
        model.addAttribute("productDto", productDto);
        model.addAttribute("navFragment", "fragment/nav/adminNav");
        model.addAttribute("content", "content/product_insert");
        return "layout";
    }

    @PostMapping("/content/product_insert")
    public String product_insert(ProductDto productDto) {
        MultipartFile file = productDto.getFile();

        String orgFileName = file.getOriginalFilename();
        String extName = orgFileName.substring(orgFileName.lastIndexOf("."));
        String saveFileName = UUID.randomUUID() + extName;
        try {
            File f = new File(UPLOADPATH + saveFileName);
            file.transferTo(f);
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }

        Product_imageDto imageDto = new Product_imageDto(0, UPLOADPATH + saveFileName, saveFileName);

        int n = productService.insert(productDto, imageDto);
        return "redirect:/content/productList";
    }

    @GetMapping("/content/product_category")
    @ResponseBody
    public List<CategoryDto> product_category(@RequestParam int parentId) {
        List<CategoryDto> list = productService.getChildrenByParentId(parentId);
        return list;
    }
}
