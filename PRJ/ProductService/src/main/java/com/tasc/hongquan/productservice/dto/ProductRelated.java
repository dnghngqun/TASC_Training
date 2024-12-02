package com.tasc.hongquan.productservice.dto;

import com.tasc.hongquan.productservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductRelated {
    private int productId;
    private List<Integer> productRelated;
}
