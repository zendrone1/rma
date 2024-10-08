package com.restaurant_management_app.rma.dto;

import com.restaurant_management_app.rma.model.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDTO {
    private Long id;            // Inclui o ID, que pode não ser necessário ao adicionar
    private String name;
    private BigDecimal price;
    private int quantity;
    private Category category;  // Aqui, a entidade completa Category pode ser usada
}
