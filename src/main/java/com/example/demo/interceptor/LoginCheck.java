package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class LoginCheck implements HandlerInterceptor {
    private static final long LIMIT = TimeUnit.MINUTES.toMillis(30); //30분

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        Integer webuserId = (session == null)
                ? null
                : (Integer) session.getAttribute("webuser_id");

        if (webuserId == null) {
            return unauthorized(request, response);
        }

        Long lastAction = (Long) session.getAttribute("lastUserActionTime");

        if (lastAction == null) {
            session.setAttribute("lastUserActionTime", System.currentTimeMillis());
            return true;
        }

        if (System.currentTimeMillis() - lastAction > LIMIT) {
            session.invalidate();
            return unauthorized(request, response);
        }

        if (!uri.startsWith("/api/localProducts")) {
            session.setAttribute("lastUserActionTime", System.currentTimeMillis());
        }

        return true;
    }

    private boolean unauthorized(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("LOGIN_REQUIRED");
        } else {
            response.sendRedirect("/login");
        }
        return false;
    }
}
