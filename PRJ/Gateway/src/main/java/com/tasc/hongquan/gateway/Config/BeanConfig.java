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
import reactor.core.publisher.Mono;

@Configuration
public class BeanConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r -> r.path("/products/**", "/categories/**", "/cart/**")
//                        .filters(f -> f.rewritePath("/products/(?<path>.*)", "/${path}")
//                                .rewritePath("/categories/(?<path>.*)", "/${path}")
//                                .rewritePath("/cart/(?<path>.*)", "/${path}"))
                        .uri("lb://product-service")).build();
    }

    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String MAX_AGE = "3600";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    HttpHeaders headers = response.getHeaders();
                    headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
                    headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                    headers.add("Access-Control-Max-Age", MAX_AGE);
                    headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }
}
