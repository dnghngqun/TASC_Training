package com.tasc.hongquan.orderservice.controllers;

import com.tasc.hongquan.orderservice.dto.OrderRequest;
import com.tasc.hongquan.orderservice.dto.ResponseObject;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.tasc.hongquan.orderservice.models.Order;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.tasc.hongquan.orderservice.services.OrderService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);


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
    public ResponseEntity<ResponseObject> addOrder(@RequestBody Order order) {
        try {
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
    public ResponseEntity<ResponseObject> updateOrderById(@RequestParam int id, @RequestBody Order order) {
        try {
            Order orderUpdate = orderService.updateOrderById(id, order);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update order successfully!", orderUpdate)
            );
        } catch (Exception e) {
            logger.error("Error to update order: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Update order failed!", null)
            );
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<ResponseObject> deleteOrderById(@RequestParam int id) {
        try {
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
    public ResponseEntity<ResponseObject> addOrderWithDetailsUsingProcedure(@RequestBody @Valid OrderRequest orderRequest, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("error", bindingResult.getAllErrors().get(0).getDefaultMessage(), null)
                );
            }
            Object payUrl = orderService.addOrderWithDetailsUsingProcedure(orderRequest.getUserId(), orderRequest.getTotalPrice(), orderRequest.getDiscountId(), orderRequest.getOrderDetails(), orderRequest.getNote(), orderRequest.getAddressId(), orderRequest.getPaymentMethod());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Add order with details successfully!", payUrl)
            );
        } catch (Exception e) {
            logger.error("Error to add order with details: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Add order with details failed!", null)
            );
        }
    }

}
