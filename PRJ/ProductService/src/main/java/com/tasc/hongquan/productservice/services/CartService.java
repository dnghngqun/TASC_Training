package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.dto.CartResponse;
import com.tasc.hongquan.productservice.models.Cart;

import java.util.List;
import java.util.Map;

public interface CartService {
    void addProductToCart(int productId, int quantity, String userId) throws Exception;

    void removeProductFromCart(int productId, String userId);

    void updateProductQuantity(int productId, int quantity, String userId);

    void clearCart(String userId);

    List<CartResponse> getCartByUserId(String userId);

    int increaseProductQuantity(int productId, String userId);

    int decreaseProductQuantity(int productId, String userId);

}
