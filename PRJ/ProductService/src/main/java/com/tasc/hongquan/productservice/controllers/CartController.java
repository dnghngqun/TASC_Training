package com.tasc.hongquan.productservice.controllers;

import com.tasc.hongquan.productservice.dto.ResponseBody;
import com.tasc.hongquan.productservice.services.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;
    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    @PostMapping("/add")
    public ResponseEntity<ResponseBody> addProductToCart(@RequestParam(value = "productId") String productIdParam, @RequestParam("quantity") String quantityParam, @RequestParam String userId) {
        try {
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);
            cartService.addProductToCart(productId, quantity, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Add product to cart successfully!!", null)
            );
        } catch (Exception e) {
            logger.error("Error when add product to cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error when add product to cart: " + e.getMessage(), null)
            );
        }
    }

    @PutMapping("/update/quantity")
    public ResponseEntity<ResponseBody> updateQuantityProduct(@RequestParam(value = "productId") String productIdParam, @RequestParam("quantity") String quantityParam, @RequestParam String userId) {
        try {
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);
            cartService.updateProductQuantity(productId, quantity, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Update product quantity successfully!!", null)
            );
        } catch (Exception e) {
            logger.error("Error when update product quantity: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error when update product quantity: " + e.getMessage(), null)
            );
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ResponseBody> clearCart(@RequestParam String userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Clear cart successfully!!", null)
            );
        } catch (Exception e) {
            logger.error("Error when clear cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error when clear cart: " + e.getMessage(), null)
            );
        }
    }
}
