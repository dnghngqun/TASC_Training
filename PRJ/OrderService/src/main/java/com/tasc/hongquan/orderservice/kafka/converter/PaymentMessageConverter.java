package com.tasc.hongquan.orderservice.kafka.converter;

import com.tasc.hongquan.orderservice.models.Payment;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;

import java.util.Collections;

public class PaymentMessageConverter extends JsonMessageConverter {
    public PaymentMessageConverter() {
        super();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(DefaultJackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("*");
        typeMapper.setIdClassMapping(Collections.singletonMap("payment", Payment.class));
        this.setTypeMapper(typeMapper);
    }
}
