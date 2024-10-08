package com.restaurant_management_app.rma.service.product;

import com.restaurant_management_app.rma.dto.ProductDTO;
import com.restaurant_management_app.rma.model.Product;
import com.restaurant_management_app.rma.request.AddProductRequest;
import com.restaurant_management_app.rma.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    List<Product>  getAllProducts();
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long id);
    List<Product> getProductByName(String productName);
    List<Product> getProductByCategory(String category);

    ProductDTO convertToDTO(Product theProduct);
}
