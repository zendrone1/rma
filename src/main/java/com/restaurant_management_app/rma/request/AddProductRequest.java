package com.restaurant_management_app.rma.request;

import com.restaurant_management_app.rma.model.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private Category category;
}
