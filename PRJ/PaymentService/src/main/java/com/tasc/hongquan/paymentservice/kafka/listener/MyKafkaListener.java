package com.tasc.hongquan.paymentservice.kafka.listener;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@KafkaListener(id = "groupA", topics = {"insertOrderTopic"})
public class MyKafkaListener {

    @KafkaHandler
    public void listenObject(String object){
        System.out.println("Received: " + object);
    }
}
