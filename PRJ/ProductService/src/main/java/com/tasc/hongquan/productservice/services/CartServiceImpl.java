package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.models.Cart;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.models.User;
import com.tasc.hongquan.productservice.repositories.CartRepository;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import com.tasc.hongquan.productservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public void addProductToCart(int productId, int quantity, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
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

    @Override
    public List<Cart> getCartByUserId(String userId) {
        return cartRepository.getCartByUserId(userId);
    }
}
