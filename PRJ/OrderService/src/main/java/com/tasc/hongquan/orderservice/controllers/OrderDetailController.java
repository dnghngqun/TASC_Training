package com.tasc.hongquan.orderservice.controllers;

import com.tasc.hongquan.orderservice.dto.ResponseObject;
import com.tasc.hongquan.orderservice.models.OrderDetail;
import com.tasc.hongquan.orderservice.services.OrderDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-details")
@AllArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addOrderDetail(@RequestBody OrderDetail orderDetail){
        try {
            orderDetailService.addOrderDetail(orderDetail);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Add order detail successfully", orderDetail)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateOrderDetail(@RequestBody OrderDetail orderDetail){
        try {
            orderDetailService.updateOrderDetail(orderDetail);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update order detail successfully", orderDetail)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteOrderDetail(@PathVariable int id){
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete order detail successfully", null)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/find-by-orderid/{id}")
    public ResponseEntity<ResponseObject> findByOrderId(@PathVariable int id){
        try {
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Find order detail by order id successfully", orderDetails)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getAllOrderDetails(){
        try {
            List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetail();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get all order details successfully", orderDetails)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", e.getMessage(), null)
            );
        }
    }
}
