package com.example.demo.controller.log;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.WebuserDto;
import com.example.demo.dto.WorkingLogDto;
import com.example.demo.service.LoginService;
import com.example.demo.service.WorkingLogService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final WorkingLogService workingLogService;

    @GetMapping("/login")
    public String loginForm(){
        return "login/form";
    }

    @PostMapping("/login/ok")
    public String loginOk(LoginDto dto, HttpSession session, Model model) {

        if(dto.getId() == null || "".equals(dto.getId())){
            model.addAttribute("error","아이디를 입력하세요!");
            return "login/form";
        }else{
            if(dto.getPassword() == null || "".equals(dto.getPassword())){
                model.addAttribute("error","비밀번호를 입력하세요!");
                return "login/form";
            }else {
                WebuserDto userDto=loginService.selectWebuser(dto.getId());

                if(userDto == null){
                    model.addAttribute("error", "존재하지 않는 아이디입니다.");
                    return "login/form";
                }else {
                    if (!userDto.getPassword().equals(dto.getPassword())){
                        model.addAttribute("error", "비밀번호가 틀렸습니다!");
                        return "login/form";
                    } else {
                        if (!"Y".equals(userDto.getIs_active())){
                            model.addAttribute("error","비활성 계정입니다.");
                            return "login/form";
                        }else {
                            //미퇴근 기록
                            WorkingLogDto openLog=workingLogService.getOpenWorkingLog(userDto.getWebuser_id());

                            if (userDto.getRole_id() == 1) {
                                session.setAttribute("webuser_id", userDto.getWebuser_id());
                                session.setAttribute("role_id", userDto.getRole_id());
                                session.setAttribute("webuserName",userDto.getWebuser_name());
                                session.setAttribute("roleMsg", "관리자");
                                session.setAttribute("openLog",openLog);
                                session.setAttribute("today", LocalDate.now());
                                model.addAttribute("navFragment", "fragment/nav/adminNav");
                                model.addAttribute("content", "notice/list");
                                return "layout";
                            } else if (userDto.getRole_id() == 2) {
                                session.setAttribute("webuser_id", userDto.getWebuser_id());
                                session.setAttribute("role_id", userDto.getRole_id());
                                session.setAttribute("webuserName",userDto.getWebuser_name());
                                session.setAttribute("roleMsg", "사무 직원");
                                session.setAttribute("openLog",openLog);
                                session.setAttribute("today", LocalDate.now());
                                model.addAttribute("navFragment", "fragment/nav/officeNav");
                                model.addAttribute("content", "notice/list");
                                return "layout";
                            } else if (userDto.getRole_id() == 3) {
                                session.setAttribute("webuser_id", userDto.getWebuser_id());
                                session.setAttribute("role_id", userDto.getRole_id());
                                session.setAttribute("webuserName",userDto.getWebuser_name());
                                session.setAttribute("roleMsg", "현장 직원");
                                session.setAttribute("openLog",openLog);
                                session.setAttribute("today", LocalDate.now());
                                model.addAttribute("navFragment", "fragment/nav/fieldNav");
                                model.addAttribute("content", "notice/list");
                                return "layout";
                            }

                            return "login/form";
                        }
                    }
                }
            }
        }
    }
}
