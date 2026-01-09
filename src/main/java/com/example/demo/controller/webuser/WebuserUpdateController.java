package com.example.demo.controller.webuser;

import com.example.demo.dto.WebuserDto;
import com.example.demo.service.WebuserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebuserUpdateController {
    private final WebuserService service;

    @GetMapping("/webuser/update")
    public String webuserUpdate(@RequestParam("webuser_id") int webuser_id,
                                Model model){
        WebuserDto dto=service.selectWebuser(webuser_id);
        model.addAttribute("dto",dto);
        return "webuserUpdate";
    }

    @PostMapping("/webuser/update")
    public String webuserUpdateOk(WebuserDto dto,
                                  RedirectAttributes r){
        boolean result=service.updateWebuser(dto);
        if(result){
            r.addFlashAttribute("status",
                                "success");
            r.addFlashAttribute("msg",
                    "계정 정보 수정 성공!");
        }else {
            r.addFlashAttribute("status",
                    "failure");
            r.addFlashAttribute("msg",
                    "계정 정보 수정 실패!");
        }
        return "redirect:/webuser/list";
    }
}
