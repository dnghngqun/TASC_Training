package com.tasc.hongquan.orderservice.kafka;

public interface ProducerService{
    void sendMessage(String topic, String message);
    void sendObject(String topic, Object object,String nameClass, Class objectName);
}
