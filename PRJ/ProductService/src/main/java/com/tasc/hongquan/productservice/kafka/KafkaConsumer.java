package com.tasc.hongquan.productservice.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.productservice.dto.ProductRelated;
import com.tasc.hongquan.productservice.models.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    @Qualifier("redisTemplate")
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "product-topic", groupId = "product-group", concurrency = "4", containerFactory = "kafkaListenerContainerFactory")
    public void cacheProduct(@Payload String payload) {
        log.info("Receive message: " + payload);
        Product product = null;
        try {
            product = objectMapper.readValue(payload, Product.class);
            log.info("Cache product");

            redisTemplate.opsForHash().put("products", String.valueOf(product.getId()), product);
            redisTemplate.expire("products", 1, TimeUnit.DAYS);
        } catch (JsonProcessingException e) {
            log.error("Error when cache product: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "related-product-topic", groupId = "related-product-group", concurrency = "4", containerFactory = "kafkaListenerContainerFactory")
    public void cacheRelatedProduct(@Payload String payload) {
        ProductRelated productRelated = null;
        try {
            productRelated = objectMapper.readValue(payload, ProductRelated.class);
            log.info("Cache related product");

            redisTemplate.opsForHash().put("related-products", String.valueOf(productRelated.getProductId()), productRelated.getProductRelated());
            redisTemplate.expire("related-products", 1, TimeUnit.DAYS);
        } catch (JsonProcessingException e) {
            log.error("Error when cache related product: " + e.getMessage());
        }
    }
}

