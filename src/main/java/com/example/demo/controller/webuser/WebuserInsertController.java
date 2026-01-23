package com.example.demo.controller.webuser;

import com.example.demo.dto.WebuserDto;
import com.example.demo.service.WebuserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebuserInsertController {
    private final WebuserService service;

    @GetMapping("/webuser/insert")
    public String webuserInsert(){
        return "webuserInsert";
    }

    @GetMapping("/webuser/idcheck")
    @ResponseBody
    public String webuserIdcheck(@RequestParam String id){

        if (id == null || "".equals(id) || id.isBlank()){
            String result="아이디를 입력하세요.";
            return result;
        }

        WebuserDto dto=service.idCheck(id);
        if(dto != null){
            String result="이미 사용 중인 아이디입니다.";
            return result;
        }else {
            String result="사용 가능한 아이디입니다.";
            return result;
        }
    }

    @PostMapping("/webuser/insert")
    public String webuserInsertOk(WebuserDto dto,
                                  RedirectAttributes r){
        if (dto.getId() == null || dto.getId().isBlank()){
            r.addFlashAttribute("status","failure");
            r.addFlashAttribute("msg","아이디를 입력하세요!");
            return "redirect:/webuser/list";
        } else {
            if (dto.getPassword() == null || dto.getPassword().isBlank()){
                r.addFlashAttribute("status","failure");
                r.addFlashAttribute("msg","비밀번호를 입력하세요!");
                return "redirect:/webuser/list";
            }else {
                if (dto.getWebuser_name() == null || dto.getWebuser_name().isBlank()){
                    r.addFlashAttribute("status","failure");
                    r.addFlashAttribute("msg","이름를 입력하세요!");
                    return "redirect:/webuser/list";
                }else {
                    if (dto.getRole_id() == null){
                        r.addFlashAttribute("status","failure");
                        r.addFlashAttribute("msg","역할을 선택하세요!");
                        return "redirect:/webuser/list";
                    } else {
                        boolean result=service.insertWebuser(dto);

                        if(result){
                            r.addFlashAttribute("status",
                                    "success");
                            r.addFlashAttribute("msg",
                                    "계정 추가 성공!");
                        }else {
                            r.addFlashAttribute("status",
                                    "failure");
                            r.addFlashAttribute("msg",
                                    "계정 추가 실패!");
                        }
                        return "redirect:/webuser/list";
                    }
                }
            }
        }
    }

    @PostMapping("/webuser/insert/excel")
    @ResponseBody
    public int webuserInsertExcel(@RequestParam("file") MultipartFile file){
        int n=service.insertExcel(file);
        return n;
    }
}
