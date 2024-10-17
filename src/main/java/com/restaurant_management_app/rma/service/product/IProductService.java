package com.restaurant_management_app.rma.service.product;

import com.restaurant_management_app.rma.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    ProductDTO addProduct(ProductDTO productDTO); // Adiciona um novo produto
    ProductDTO getProductById(Long id);            // Recupera um produto pelo ID
    List<ProductDTO> getAllProducts();             // Recupera todos os produtos
    ProductDTO updateProduct(Long id, ProductDTO productDTO);  // Atualiza um produto existente
    void deleteProductById(Long id);                   // Remove um produto pelo ID
}
