package com.tasc.hongquan.gateway.Config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Configuration
public class BeanConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ProductService", r -> r.path("/api/v1/products/**", "/api/v1/categories/**", "/api/v1/cart/**")
//                        .filters(f -> f.rewritePath("/products/(?<path>.*)", "/${path}")
//                                .rewritePath("/categories/(?<path>.*)", "/${path}")
//                                .rewritePath("/cart/(?<path>.*)", "/${path}"))
                        .uri("lb://ProductService"))
                .route("PaymentService", r -> r.path("/api/v1/payments/**")
                        .uri("lb://PaymentService"))
                .route("OrderService", r -> r.path("/api/v1/orders/**", "/api/v1/order-details/**", "/api/v1/discounts/**")
                        .uri("lb://OrderService"))
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://user-service"))
                .build();
    }

    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
    private static final String[] ALLOWED_ORIGINS = {"http://localhost:4200", "http://localhost:4201"};
    private static final String MAX_AGE = "3600"; // 1 hour

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            ServerHttpResponse response = ctx.getResponse();
            HttpHeaders headers = response.getHeaders();

            // check origin valid
            if (!headers.containsKey("Access-Control-Allow-Origin")) {
                String origin = request.getHeaders().getOrigin();
                if (origin != null && Arrays.asList(ALLOWED_ORIGINS).contains(origin)) {
                    headers.add("Access-Control-Allow-Origin", origin);
                }
                headers.add("Access-Control-Allow-Credentials", "true");
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                headers.add("Access-Control-Max-Age", MAX_AGE);
            }

            // Xử lý request OPTIONS
            if (CorsUtils.isCorsRequest(request) && request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }

            return chain.filter(ctx);
        };
    }
}
