package com.tasc.hongquan.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRelated {
    private int productId;
    private List<String> productRelated;
}
