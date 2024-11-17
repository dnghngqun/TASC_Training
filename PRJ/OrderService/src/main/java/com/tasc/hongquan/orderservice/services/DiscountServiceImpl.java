package com.tasc.hongquan.orderservice.services;

import com.tasc.hongquan.orderservice.models.Discount;
import com.tasc.hongquan.orderservice.repositories.DiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
@Service
@AllArgsConstructor
public class DiscountServiceImpl implements DiscountService{
    private final DiscountRepository discountRepository;
    @Override
    public void addDiscount(Discount discount) {
        discount.setCreatedAt(Instant.now());
        discount.setUpdatedAt(Instant.now());
        discountRepository.addDiscount(discount);

    }

    @Override
    public void updateDiscount(Discount discountUpdate) {
        Discount discount = discountRepository.findById(discountUpdate.getId());
        if(discount == null){
            throw new RuntimeException("Discount not found");
        }
        if(discountUpdate.getCode() != null){
            discount.setCode(discountUpdate.getCode());
        }
        if(discountUpdate.getAmount() != null){
            discount.setAmount(discountUpdate.getAmount());
        }
        if (discountUpdate.getQuantity() != null) {
            discount.setQuantity(discountUpdate.getQuantity());
        }
        if (discountUpdate.getExpires() != null) {
            discount.setExpires(discountUpdate.getExpires());
        }
        if (discountUpdate.getDescription() != null) {
            discount.setDescription(discountUpdate.getDescription());
        }
        discount.setUpdatedAt(Instant.now());
        discountRepository.updateDiscount(discount);
    }

    @Override
    public void deleteDiscount(int id) {
        discountRepository.deleteDiscount(id);
    }

    @Override
    public Discount findById(int id) {
        return discountRepository.findById(id);
    }

    @Override
    public Discount findByCode(String code) {
        return discountRepository.findByCode(code);
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.getAllDiscounts();
    }
}
