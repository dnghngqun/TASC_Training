package com.tasc.hongquan.orderservice.controllers;

import com.tasc.hongquan.orderservice.dto.OrderRequest;
import com.tasc.hongquan.orderservice.dto.ResponseObject;
import lombok.AllArgsConstructor;
import com.tasc.hongquan.orderservice.models.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tasc.hongquan.orderservice.services.OrderService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController{
    private final OrderService orderService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get all orders successfully!", orders)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Get all orders failed!", null)
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addOrder(@RequestBody Order order){
        try{
            orderService.addOrder(order);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Add order successfully!", order)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Add order failed!", null)
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateOrderById(@RequestParam int id, @RequestBody Order order){
        try{
            orderService.updateOrderById(id, order);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update order successfully!", order)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Update order failed!", null)
            );
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<ResponseObject> deleteOrderById(@RequestParam int id){
        try{
            orderService.deleteOrderById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete order successfully!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Delete order failed!", null)
            );
        }
    }

    @PostMapping("/addOrder")
    public ResponseEntity<ResponseObject> addOrderWithDetailsUsingProcedure(@RequestBody OrderRequest orderRequest){
        try{
            orderService.addOrderWithDetailsUsingProcedure(orderRequest.getUserId(), orderRequest.getTotalPrice(), orderRequest.getDiscountId(), orderRequest.getOrderDetails());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Add order with details successfully!", orderRequest)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Add order with details failed!", null)
            );
        }
    }

}
