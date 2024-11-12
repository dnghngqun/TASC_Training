package com.tasc.hongquan.productservice.controllers;

import com.tasc.hongquan.productservice.dto.CartRequest;
import com.tasc.hongquan.productservice.dto.ResponseBody;
import com.tasc.hongquan.productservice.services.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
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
    public ResponseEntity<ResponseBody> addProductToCart(@RequestBody CartRequest cartRequest) {
        try {
            logger.info("Cart request: " + cartRequest.getUserId() + " - " + cartRequest.getProductId() + " - " + cartRequest.getQuantity());
            cartService.addProductToCart(cartRequest.getProductId(), cartRequest.getQuantity(), cartRequest.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Add product to cart successfully!!", cartRequest)
            );
        } catch (Exception e) {
            logger.error("Error when add product to cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error when add product to cart: " + e.getMessage(), null)
            );
        }
    }

    @PutMapping("/product/increase")
    public ResponseEntity<ResponseBody> increaseProductQuantity(@RequestBody CartRequest cartRequest) {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Increase product quantity successfully!!", cartService.increaseProductQuantity(cartRequest.getProductId(), cartRequest.getUserId()))
            );
        } catch (Exception e) {
            logger.error("Error when increase product quantity: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error when increase product quantity: " + e.getMessage(), null)
            );
        }
    }

    @PutMapping("/product/decrease")
    public ResponseEntity<ResponseBody> decreaseProductQuantity(@RequestBody CartRequest cartRequest) {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Decrease product quantity successfully!!", cartService.decreaseProductQuantity(cartRequest.getProductId(), cartRequest.getUserId()))
            );
        } catch (Exception e) {
            logger.error("Error when decrease product quantity: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error when decrease product quantity: " + e.getMessage(), null)
            );
        }
    }

    @PutMapping("/product/remove")
    public ResponseEntity<ResponseBody> removeProduct(@RequestBody CartRequest cartRequest) {
        try {
            cartService.removeProductFromCart(cartRequest.getProductId(), cartRequest.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Remove product from cart successfully!!", cartRequest)
            );
        } catch (Exception e) {
            logger.error("Error when remove product from cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error when remove product from cart: " + e.getMessage(), null)
            );
        }
    }


    @PutMapping("/update/quantity")
    public ResponseEntity<ResponseBody> updateQuantityProduct(@RequestBody CartRequest cartRequest) {
        try {
            cartService.updateProductQuantity(cartRequest.getProductId(), cartRequest.getQuantity(), cartRequest.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Update product quantity successfully!!", cartRequest)
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

    @GetMapping("/get")
    public ResponseEntity<ResponseBody> getCartByUserId(@RequestParam String userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseBody("ok", "Get cart by user id successfully!!", cartService.getCartByUserId(userId))
            );
        } catch (Exception e) {
            logger.error("Error when get cart by user id: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseBody("error", "Error when get cart by user id: " + e.getMessage(), null)
            );
        }
    }
}
