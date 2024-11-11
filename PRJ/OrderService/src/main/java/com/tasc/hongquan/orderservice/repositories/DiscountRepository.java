package com.tasc.hongquan.orderservice.repositories;

import com.tasc.hongquan.orderservice.models.Discount;

import java.util.List;

public interface DiscountRepository {
    void addDiscount(Discount discount);
    void updateDiscount(Discount discount);
    void deleteDiscount(int id);
    Discount findById(int id);
    Discount findByCode(String code);
    List<Discount> getAllDiscounts();

}
