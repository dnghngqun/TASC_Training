package com.tasc.hongquan.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ProductService", url = "http://localhost:8081")
public interface ProductClient {

}
