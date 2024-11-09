package com.tasc.hongquan.productservice.repositories;

import com.tasc.hongquan.productservice.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.product.id = :productId AND c.user.userId = :userId")
    void removeProductFromCart(int productId, String userId);

    @Modifying
    @Query("update Cart c set c.quantity = :quantity where c.product.id = :productId and c.user.userId = :userId")
    void updateQuantityProductFromCart(int productId, int quantity, String userId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.userId = :userId")
    void clearCartByUserId(String userId);

    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    List<Cart> getCartByUserId(String userId);
}
