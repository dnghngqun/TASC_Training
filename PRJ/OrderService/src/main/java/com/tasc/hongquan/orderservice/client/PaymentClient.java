package com.tasc.hongquan.orderservice.client;

import com.tasc.hongquan.orderservice.dto.ResponseObject;
import com.tasc.hongquan.orderservice.models.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="PaymentService", url = "localhost:8083/api/v1/payments")
public interface PaymentClient {
    @PostMapping("/add")
    ResponseEntity<ResponseObject> addPayment(@RequestBody Payment payment);

    @PostMapping("/update")
    ResponseEntity<ResponseObject> updatePayment(@RequestBody Payment payment);

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updatePaymentById(@PathVariable int id, @RequestBody Payment paymentUpdate);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ResponseObject> deletePaymentById(@PathVariable int id);
}
