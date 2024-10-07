package com.restaurant_management_app.rma.request;

import com.restaurant_management_app.rma.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private Category category;
}
