package com.tasc.hongquan.productservice.kafka;

import com.tasc.hongquan.productservice.dto.ProductRelated;
import com.tasc.hongquan.productservice.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendProduct(Product product) {
        kafkaTemplate.send("product-topic", product);
    }

    public void sendRelatedProduct(ProductRelated productRelated) {
        kafkaTemplate.send("related-product-topic", productRelated);
    }


}
