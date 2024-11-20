package com.tasc.hongquan.paymentservice.client;

import com.tasc.hongquan.paymentservice.dto.ResponseObject;
import com.tasc.hongquan.paymentservice.models.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "OrderService", url = "localhost:8084/api/v1/orders")
public interface OrderClient {
    @PutMapping("/update")
    ResponseEntity<ResponseObject> updateOrderById(@RequestParam int id, @RequestBody Order order);
}