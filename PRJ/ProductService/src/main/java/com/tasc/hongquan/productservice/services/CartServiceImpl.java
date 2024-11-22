package com.tasc.hongquan.productservice.services;

import com.tasc.hongquan.productservice.dto.CartResponse;
import com.tasc.hongquan.productservice.models.Cart;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.models.User;
import com.tasc.hongquan.productservice.repositories.CartRepository;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import com.tasc.hongquan.productservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    //KEY moi gio hang se la cart:userId
    public static final String CART_KEY_PREFIX = "cart:";
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addProductToCart(int productId, int quantity, String userId) {
        String cartKey = CART_KEY_PREFIX + userId;

        // Lấy số lượng hiện tại của sản phẩm từ Redis (nếu có)
        Object currentQuantityObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(productId));

        Integer currentQuantity = null;
        if (currentQuantityObj != null) {
            // Nếu sản phẩm đã có trong giỏ, lấy số lượng hiện tại
            currentQuantity = Integer.parseInt(currentQuantityObj.toString());
        }

        // Nếu sản phẩm đã có trong giỏ, cộng thêm số lượng
        if (currentQuantity != null) {
            quantity += currentQuantity;
        }

        // Cập nhật giỏ hàng với số lượng mới
        redisTemplate.opsForHash().put(cartKey, String.valueOf(productId), String.valueOf(quantity));
        redisTemplate.opsForSet().add("activeCarts", userId);
    }


    @Override
    public int increaseProductQuantity(int productId, String userId) {
        String cartKey = CART_KEY_PREFIX + userId;

        // Lấy số lượng hiện tại của sản phẩm
        Object currentQuantityObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(productId));
        Integer currentQuantity = null;

        if (currentQuantityObj != null) {
            currentQuantity = Integer.parseInt(currentQuantityObj.toString());
        }

        // Nếu sản phẩm đã có trong giỏ, tăng thêm 1
        if (currentQuantity != null) {
            redisTemplate.opsForHash().put(cartKey, String.valueOf(productId), String.valueOf(currentQuantity + 1));
        }
        return currentQuantity + 1;
    }

    @Override
    public int decreaseProductQuantity(int productId, String userId) {
        String cartKey = CART_KEY_PREFIX + userId;

        Object currentQuantityObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(productId));
        Integer currentQuantity = null;

        if (currentQuantityObj != null) {
            currentQuantity = Integer.parseInt(currentQuantityObj.toString());
        }

        if (currentQuantity != null && currentQuantity > 1) {
            redisTemplate.opsForHash().put(cartKey, String.valueOf(productId), String.valueOf(currentQuantity - 1));
            return currentQuantity - 1;
        } else {
            redisTemplate.opsForHash().delete(cartKey, String.valueOf(productId));
            return 0;
        }
    }


    @Override
    public List<CartResponse> getCartByUserId(String userId) {
        String cartKey = CART_KEY_PREFIX + userId;
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(cartKey);
        if (cart.isEmpty()) {
            //get cart from database
            List<Cart> carts = cartRepository.getCartByUserId(userId);
            carts.forEach(item -> redisTemplate.opsForHash().put(cartKey, String.valueOf(item.getProduct().getId()), String.valueOf(item.getQuantity())));
            cart = redisTemplate.opsForHash().entries(cartKey);
        }

        List<CartResponse> cartResponses = new ArrayList<>();
        cart.forEach((productId, quantity) -> {
            Product product = productRepository.findById(Integer.parseInt(productId.toString())).orElseThrow(() -> new RuntimeException("Product not found"));
            cartResponses.add(new CartResponse(product, Integer.parseInt(quantity.toString())));
        });
        return cartResponses;
    }


    @Override
    public void clearCart(String userId) {
        String cartKey = CART_KEY_PREFIX + userId;
        redisTemplate.delete(cartKey);
        cartRepository.clearCartByUserId(userId);
        // Xóa userId khỏi set activeCarts khi giỏ hàng đã được xóa
        redisTemplate.opsForSet().remove("activeCarts", userId);
    }


    @Override
    public void removeProductFromCart(int productId, String userId) {
        String cartKey = CART_KEY_PREFIX + userId;
        redisTemplate.opsForHash().delete(cartKey, String.valueOf(productId));
    }


    //    @Override
//    public void updateProductQuantity(int productId, int quantity, String userId) {
//        if (quantity > 0) {
//            cartRepository.updateQuantityProductFromCart(productId, quantity, userId);
//        } else {
//            cartRepository.removeProductFromCart(productId, userId);
//        }
//    }
    @Override
    public void updateProductQuantity(int productId, int quantity, String userId) {
        String cartKey = CART_KEY_PREFIX + userId;

        if (quantity > 0) {
            redisTemplate.opsForHash().put(cartKey, productId, quantity);
        } else {
            // Xóa sản phẩm khỏi giỏ hàng
            redisTemplate.opsForHash().delete(cartKey, productId);
        }
    }


//    @Override
//    public void clearCart(String userId) {
//        cartRepository.clearCartByUserId(userId);
//    }

//    @Override
//    public List<Cart> getCartByUserId(String userId) {
//        return cartRepository.getCartByUserId(userId);
//    }
}
