package com.tasc.hongquan.apigateway.superAdmin;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SuperAdminAuthenticationFilter extends OncePerRequestFilter {
    private static final String SUPER_ADMIN_USERNAME = "superadmin";
    private static final String SUPER_ADMIN_PASSWORD = "hongquan";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Chỉ kiểm tra khi cần thiết
        if (shouldAuthenticate(request)) {
            String username = request.getHeader("Username");
            String password = request.getHeader("Password");

            if (SUPER_ADMIN_USERNAME.equals(username) && SUPER_ADMIN_PASSWORD.equals(password)) {
                Authentication authentication = new SuperAdminAuthenticationToken();
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    private boolean shouldAuthenticate(HttpServletRequest request) {
        // Kiểm tra nếu request là của Super Admin
        String path = request.getRequestURI();
        return path.startsWith("/admin");
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Áp dụng filter này chỉ cho những endpoint yêu cầu quyền superadmin
        return !path.startsWith("/admin");
    }



}
