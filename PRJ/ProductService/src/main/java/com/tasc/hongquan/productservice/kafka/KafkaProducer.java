package com.tasc.hongquan.productservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasc.hongquan.productservice.dto.ProductRelated;
import com.tasc.hongquan.productservice.models.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendProduct(Product product) {
        try {
            log.info("Send product to kafka");
            String json = objectMapper.writeValueAsString(product);
            kafkaTemplate.send("product-topic", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRelatedProduct(ProductRelated productRelated) {
        try {
            String json = objectMapper.writeValueAsString(productRelated);
            kafkaTemplate.send("related-product-topic", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
