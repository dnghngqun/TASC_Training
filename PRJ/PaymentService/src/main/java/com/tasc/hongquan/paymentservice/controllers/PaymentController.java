package com.tasc.hongquan.paymentservice.controllers;

import com.tasc.hongquan.paymentservice.dto.ResponseObject;
import com.tasc.hongquan.paymentservice.models.Order;
import com.tasc.hongquan.paymentservice.models.Payment;
import com.tasc.hongquan.paymentservice.services.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);


    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllPayments() {
        try {
            return ResponseEntity.ok(new ResponseObject("ok", "Get all payments successfully", paymentService.getAllPayments()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Get all payments failed", null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addPayment(@RequestBody Payment payment) {
        try {
            paymentService.addPayment(payment);
            return ResponseEntity.ok(new ResponseObject("ok", "Add payment successfully", null));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Add payment failed", null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updatePaymentById(@PathVariable int id, @RequestBody Payment paymentUpdate) {
        try {
            paymentService.updatePaymentById(id, paymentUpdate);
            return ResponseEntity.ok(new ResponseObject("ok", "Update payment successfully", null));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Update payment failed", null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deletePaymentById(@PathVariable int id) {
        try {
            paymentService.deletePaymentById(id);
            return ResponseEntity.ok(new ResponseObject("ok", "Delete payment successfully", null));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(500).body(new ResponseObject("error", "Delete payment failed", null));
        }
    }







}
