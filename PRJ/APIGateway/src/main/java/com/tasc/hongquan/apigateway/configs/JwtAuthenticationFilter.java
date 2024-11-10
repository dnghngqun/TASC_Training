package com.tasc.hongquan.apigateway.configs;

import com.tasc.hongquan.apigateway.Jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;
    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            logger.info("Bearer token: {}", bearerToken.substring(7));
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {
        logger.info("Request URI: {}", request.getRequestURI());
        String[] publicPaths = {"/users/public/**", "/products/**"};
        for (String path : publicPaths) {
            if (antPathMatcher.match(path, request.getRequestURI())) {
                logger.info("Request is public.");
                filterChain.doFilter(request, response);
                return;
            }
        }

//        String token = getJWTFromRequest(request);
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.warn("Cookie not available.");
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    String encodeToken = cookie.getValue();
                    byte[] decodedBytes = Base64.getDecoder().decode(encodeToken);
                    token = new String(decodedBytes);
                }
            }
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmailFromJWT(token);
                String role = jwtTokenProvider.getRoleFromJWT(token);
                logger.info("Email: {}, Role: {}", email, role);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                logger.warn("Invalid token or not available.");
            }
        }
        filterChain.doFilter(request, response);
    }
}
