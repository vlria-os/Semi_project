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
                            //어제 퇴근 기록 - 있어야 출근 버튼 활성화
                            WorkingLogDto checkOutDto=workingLogService.getYesterdayCheckOutLogOne(userDto.getWebuser_id());

                            //오늘 출근 기록 - 없어야 출근 버튼 활성화 (있으면 퇴근 버튼이 보여야 함)
                            WorkingLogDto checkInDto=workingLogService.getTodayCheckInLogOne(userDto.getWebuser_id());

                            //오늘 퇴근 기록 - 있으면 퇴근 버튼 비활성화
                            WorkingLogDto todayCheckOutDto=workingLogService.getTodayCheckOutLogOne(userDto.getWebuser_id());

                            System.out.println("user=" + userDto.getWebuser_id());
                            System.out.println("yesterday=" + checkOutDto);
                            System.out.println("todayIn=" + checkInDto);
                            System.out.println("todayOut=" + todayCheckOutDto);

                            if (userDto.getRole_id() == 1) {
                                session.setAttribute("webuser_id", userDto.getWebuser_id());
                                session.setAttribute("role_id", userDto.getRole_id());
                                session.setAttribute("roleMsg", "관리자");
                                session.setAttribute("YesterdayCheckOutLog",checkOutDto);
                                session.setAttribute("TodayCheckInLog",checkInDto);
                                session.setAttribute("TodayCheckOutLog",todayCheckOutDto);
                                model.addAttribute("navFragment", "fragment/nav/adminNav");
                                model.addAttribute("content", "content/notice");
                                return "layout";
                            } else if (userDto.getRole_id() == 2) {
                                session.setAttribute("webuser_id", userDto.getWebuser_id());
                                session.setAttribute("role_id", userDto.getRole_id());
                                session.setAttribute("roleMsg", "사무 직원");
                                session.setAttribute("YesterdayCheckOutLog",checkOutDto);
                                session.setAttribute("TodayCheckInLog",checkInDto);
                                session.setAttribute("TodayCheckOutLog",todayCheckOutDto);
                                model.addAttribute("navFragment", "fragment/nav/officeNav");
                                model.addAttribute("content", "content/notice");
                                return "layout";
                            } else if (userDto.getRole_id() == 3) {
                                session.setAttribute("webuser_id", userDto.getWebuser_id());
                                session.setAttribute("role_id", userDto.getRole_id());
                                session.setAttribute("roleMsg", "현장 직원");
                                session.setAttribute("YesterdayCheckOutLog",checkOutDto);
                                session.setAttribute("TodayCheckInLog",checkInDto);
                                session.setAttribute("TodayCheckOutLog",todayCheckOutDto);
                                model.addAttribute("navFragment", "fragment/nav/fieldNav");
                                model.addAttribute("content", "content/notice");
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
