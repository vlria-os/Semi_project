package com.example.demo.controller.log;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.WebuserDTO;
import com.example.demo.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService service;

    @GetMapping("/")
    public String loginForm(){
        return "login/form";
    }

    @PostMapping("/login/ok")
    public String loginOk(LoginDTO dto, HttpSession session, Model model){
        String id=dto.getId();
        WebuserDTO userDTO=service.selectWebuser(id);
        if(userDTO == null){
            model.addAttribute("error","존재하지 않는 아이디입니다.");
            return "login/form";
        }else {
            if(!userDTO.getPassword().equals(dto.getPassword())){
                model.addAttribute("error","비밀번호가 틀렸습니다!");
                return "login/form";
            }else{
                if(userDTO.getRole_id() == 1){
                    session.setAttribute("webuser_id",userDTO.getWebuser_id());
                    session.setAttribute("role_id",userDTO.getRole_id());
                    session.setAttribute("roleMsg","관리자 모드");
                    return "redirect:/main";
                }else {
                    session.setAttribute("webuser_id",userDTO.getWebuser_id());
                    session.setAttribute("role_id",userDTO.getRole_id());
                    session.setAttribute("roleMsg","직원 모드");
                    return "redirect:/main";
                }
            }
        }
    }
}
