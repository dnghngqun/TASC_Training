package com.tasc.hongquan.paymentservice.client;

import com.tasc.hongquan.paymentservice.dto.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ConcurrentHashMap;

@FeignClient(name = "ProductService", url = "localhost:8081/api/v1")
public interface ProductClient {
    @PutMapping("/products/updateStock")
    ResponseEntity<ResponseObject> updateStockProduct(@RequestBody ConcurrentHashMap<Integer, Integer> productStock);

    @DeleteMapping("/cart/clear")
    ResponseEntity<ResponseObject> clearCart(@RequestParam String userId);
}
