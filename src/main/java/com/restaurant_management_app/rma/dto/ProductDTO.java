package com.restaurant_management_app.rma.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private Long categoryId;        // ID da categoria existente
    private String categoryName;    // Nome da nova categoria, se a existente não for encontrada
}
