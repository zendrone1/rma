package com.restaurant_management_app.rma.service.product;

import com.restaurant_management_app.rma.dto.ProductDTO;
import com.restaurant_management_app.rma.exceptions.ProductNotFoundException;
import com.restaurant_management_app.rma.model.Category;
import com.restaurant_management_app.rma.model.Product;
import com.restaurant_management_app.rma.repository.CategoryRepository;
import com.restaurant_management_app.rma.repository.ProductRepository;
import com.restaurant_management_app.rma.request.AddProductRequest;
import com.restaurant_management_app.rma.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    @Autowired
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private Product createProduct(AddProductRequest request,Category category) {
        return new Product(
                request.getName(),
                request.getPrice(),
                request.getQuantity(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {throw new ProductNotFoundException("Product Not found");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;




    }

    @Override
    public List<Product> getProductByName(String productName) {
        return productRepository.findByName(productName);
    }

    public List<Product> getProductByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        if (category != null) {
            return productRepository.findByCategory(category);
        }
        return List.of(); // Retorna uma lista vazia se a categoria não for encontrada
    }


    public ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .build();
    }
}
