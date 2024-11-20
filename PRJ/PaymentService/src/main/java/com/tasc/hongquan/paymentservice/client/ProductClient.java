package com.tasc.hongquan.paymentservice.client;

import com.tasc.hongquan.paymentservice.dto.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.ConcurrentHashMap;

@FeignClient(name = "ProductService", url = "localhost:8081/api/v1/products")
public interface ProductClient {
    @PutMapping("/updateStock")
    ResponseEntity<ResponseObject> updateStockProduct(@RequestBody ConcurrentHashMap<Integer, Integer> productStock);
}
