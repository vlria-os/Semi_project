package com.example.demo.controller.log;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.WebuserDto;
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

    @GetMapping("/login")
    public String loginForm(){
        return "login/form";
    }

    @PostMapping("/login/ok")
    public String loginOk(LoginDto dto, HttpSession session, Model model) {
        String id = dto.getId();

        WebuserDto userDto = service.selectWebuser(id);
        if (userDto == null) {
            model.addAttribute("error", "존재하지 않는 아이디입니다.");
            return "login/form";
        }
            if (!userDto.getPassword().equals(dto.getPassword())) {
                model.addAttribute("error", "비밀번호가 틀렸습니다!");
                return "login/form";
            } else {
                session.setAttribute("lastUserActionTime", System.currentTimeMillis());

                if (userDto.getRole_id() == 1) {
                    session.setAttribute("webuser_id", userDto.getWebuser_id());
                    session.setAttribute("role_id", userDto.getRole_id());
                    session.setAttribute("roleMsg", "관리자");
                    model.addAttribute("navFragment", "fragment/nav/adminNav");
                    model.addAttribute("content", "content/notice");
                    return "layout";
                } else if (userDto.getRole_id() == 2) {
                    session.setAttribute("webuser_id", userDto.getWebuser_id());
                    session.setAttribute("role_id", userDto.getRole_id());
                    session.setAttribute("roleMsg", "사무 직원");
                    model.addAttribute("navFragment", "fragment/nav/officeNav");
                    model.addAttribute("content", "content/notice");
                    return "layout";
                } else if (userDto.getRole_id() == 3) {
                    session.setAttribute("webuser_id", userDto.getWebuser_id());
                    session.setAttribute("role_id", userDto.getRole_id());
                    session.setAttribute("roleMsg", "현장 직원");
                    model.addAttribute("navFragment", "fragment/nav/fieldNav");
                    model.addAttribute("content", "content/notice");
                    return "layout";
                }
            }

            return "redirect:/login/form";

    }
}
