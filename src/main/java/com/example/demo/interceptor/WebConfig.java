package com.example.demo.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final LoginCheck loginCheck;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheck)
                .addPathPatterns("/**")          // 전체 적용
                .excludePathPatterns(
                        "/login",
                        "/login/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/error",
                        "/payment/guest/**"
                );
    }
}
