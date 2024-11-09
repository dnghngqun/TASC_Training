package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.models.Cart;

import java.util.List;

public interface CartService {
    void addProductToCart(int productId, int quantity, String userId) throws Exception;

    void removeProductFromCart(int productId, String userId);

    void updateProductQuantity(int productId, int quantity, String userId);

    void clearCart(String userId);

    List<Cart> getCartByUserId(String userId);

}
