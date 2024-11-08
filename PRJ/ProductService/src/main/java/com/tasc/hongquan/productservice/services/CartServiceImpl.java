package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.models.Cart;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.models.User;
import com.tasc.hongquan.productservice.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;


    @Override
    public void addProductToCart(int productId, int quantity, String userId) {
        User user = new User().builder()
                .userId(userId)
                .build();
        Product product = new Product().builder()
                .id(productId)
                .build();
        Cart cart = new Cart().builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();
        cartRepository.save(cart);
    }

    @Override
    public void removeProductFromCart(int productId, String userId) {
        cartRepository.removeProductFromCart(productId, userId);
    }

    @Override
    public void updateProductQuantity(int productId, int quantity, String userId) {
        if (quantity > 0) {
            cartRepository.updateQuantityProductFromCart(productId, quantity, userId);
        } else {
            cartRepository.removeProductFromCart(productId, userId);
        }
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.clearCartByUserId(userId);
    }
}
