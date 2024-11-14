package com.tasc.hongquan.gomsuserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GomsuServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GomsuServerApplication.class, args);
    }

}
