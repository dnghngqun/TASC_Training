package com.tasc.hongquan.productservice.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.productservice.dto.ProductRelated;
import com.tasc.hongquan.productservice.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    @Qualifier("redisTemplate")
    private final RedisTemplate redisTemplate;


    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "product-topic", groupId = "product-group", concurrency = "4")
    public void cacheProduct(Product product) {
        redisTemplate.opsForHash().put("products", String.valueOf(product.getId()), product);
        redisTemplate.expire("products", 1, TimeUnit.DAYS);
    }

    @KafkaListener(topics = "related-product-topic", groupId = "related-product-group", concurrency = "4")
    public void cacheRelatedProduct(ProductRelated productRelated) {
        redisTemplate.opsForHash().put("related-products", String.valueOf(productRelated.getProductId()), productRelated.getProductRelated());
        redisTemplate.expire("related-products", 1, TimeUnit.DAYS);
    }
}
