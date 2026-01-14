package com.example.demo.controller;

import com.example.demo.service.ConfirmListService;
import com.example.demo.service.Ref_ExpService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory.model;

@Controller
@RequiredArgsConstructor
public class confirmListController {
    private final ConfirmListService confirmListService;

    @GetMapping("/content/confirmList")
    public String confirmList(Model model,
                              HttpSession session){
        if((int)session.getAttribute("role_id")==1){
            model.addAttribute("list", confirmListService.Confirm_in_List());
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/confirmList");
        }else if((int)session.getAttribute("role_id")==2) {
            model.addAttribute("list", confirmListService.Confirm_in_List());
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "content/confirmList");
        }else{
            model.addAttribute("list", confirmListService.Confirm_in_List()); //수정 필요
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "content/confirmList");
        }

        return "layout";
    }

    @GetMapping("/content/confirmList/out")
    public String confirmList_out(Model model,
                              HttpSession session){
        if((int)session.getAttribute("role_id")==1){
            model.addAttribute("list", confirmListService.Confirm_out_List());
            model.addAttribute("navFragment", "fragment/nav/adminNav");
            model.addAttribute("content", "content/confirmList_out");
        }else if((int)session.getAttribute("role_id")==2) {
            model.addAttribute("list", confirmListService.Confirm_out_List());
            model.addAttribute("navFragment", "fragment/nav/officeNav");
            model.addAttribute("content", "content/confirmList_out");
        }else{
            model.addAttribute("list", confirmListService.Confirm_out_List()); //수정 필요
            model.addAttribute("navFragment", "fragment/nav/fieldNav");
            model.addAttribute("content", "content/confirmList_out");
        }
        return "layout";
    }

}
