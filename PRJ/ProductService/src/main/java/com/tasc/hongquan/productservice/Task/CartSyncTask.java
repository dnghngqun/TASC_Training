package com.tasc.hongquan.productservice.Task;

import com.tasc.hongquan.productservice.models.Cart;
import com.tasc.hongquan.productservice.models.Product;
import com.tasc.hongquan.productservice.models.User;
import com.tasc.hongquan.productservice.repositories.CartRepository;
import com.tasc.hongquan.productservice.repositories.ProductRepository;
import com.tasc.hongquan.productservice.repositories.UserRepository;
import com.tasc.hongquan.productservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tasc.hongquan.productservice.services.CartServiceImpl.CART_KEY_PREFIX;

@Component
public class CartSyncTask {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(fixedRate = 1000 * 60 * 10) // 10 minutes
    @Scheduled(fixedRate = 600000)  // Đồng bộ mỗi 10 phút
    public void syncCartFromRedisToDb() {
        List<String> userIds = (List<String>) redisTemplate
                .opsForSet()
                .members("activeCarts")
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        for (String userId : userIds) {
            String cartKey = CART_KEY_PREFIX + userId;

            // Kiểm tra xem giỏ hàng có trong Redis không
            Map<Object, Object> cart = redisTemplate.opsForHash().entries(cartKey);

            if (!cart.isEmpty()) {
                // Nếu giỏ hàng có trong Redis, đồng bộ dữ liệu vào DB
                cart.forEach((productId, quantity) -> {
                    // Tạo hoặc cập nhật giỏ hàng trong DB
                    int productIdInt = Integer.parseInt(productId.toString());
                    Cart cartItem = new Cart();
                    cartItem.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
                    cartItem.setProduct(productRepository.findById(productIdInt).orElseThrow(() -> new RuntimeException("Product not found")));
                    cartItem.setQuantity(Integer.parseInt(quantity.toString()));

                    cartRepository.save(cartItem);
                });

                // Sau khi đồng bộ, có thể xóa giỏ hàng trong Redis
                redisTemplate.delete(cartKey);
            }
        }
    }


}
