package com.tasc.hongquan.orderservice.kafka;

import com.tasc.hongquan.orderservice.kafka.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements ProducerService{
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate ;
    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public void sendObject(String topic, Object object,String nameClass, Class objectName) {
        kafkaTemplate.send(topic, object);
        kafkaTemplate.setMessageConverter(new MessageConverter(nameClass,objectName));
    }
}
