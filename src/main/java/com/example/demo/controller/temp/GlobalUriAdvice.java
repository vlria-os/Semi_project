package com.example.demo.controller.temp;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUriAdvice {
    @ModelAttribute("currentUri")
    public String addCurrentUri(HttpServletRequest request){
        String uri= request.getRequestURI();
        return (uri == null ) ? "/" : uri;
    }
}
