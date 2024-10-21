package com.restaurant_management_app.rma.service.product;

import com.restaurant_management_app.rma.dto.ProductDTO;
import com.restaurant_management_app.rma.exceptions.ProductNotFoundException;
import com.restaurant_management_app.rma.model.Category;
import com.restaurant_management_app.rma.model.Product;
import com.restaurant_management_app.rma.repository.CategoryRepository;
import com.restaurant_management_app.rma.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Category category;

        // Verifica se categoryId foi fornecido
        if (productDTO.getCategoryId() != null) {
            // Tenta encontrar a categoria pelo ID
            Optional<Category> categoryOpt = categoryRepository.findById(productDTO.getCategoryId());
            if (categoryOpt.isPresent()) {
                category = categoryOpt.get();
            } else {
                throw new ProductNotFoundException("Category not found with the given ID.");
            }
        } else if (productDTO.getCategoryName() != null && !productDTO.getCategoryName().isEmpty()) {
            // Verifica se a categoria já existe pelo nome
            Optional<Category> existingCategoryOpt = categoryRepository.findByName(productDTO.getCategoryName());
            if (existingCategoryOpt.isPresent()) {
                // Usa a categoria existente
                category = existingCategoryOpt.get();
            } else {
                // Cria uma nova categoria se não existir
                category = new Category(productDTO.getCategoryName());
                category = categoryRepository.save(category); // Salva a nova categoria no banco
            }
        } else {
            throw new IllegalArgumentException("Either categoryId or categoryName must be provided.");
        }

        // Cria o produto com a categoria encontrada ou criada
        Product product = new Product(productDTO.getName(), productDTO.getPrice(), productDTO.getQuantity(), category);
        Product savedProduct = productRepository.save(product);

        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());

        Optional<Category> categoryOpt = categoryRepository.findById(productDTO.getCategoryId());
        if (categoryOpt.isPresent()) {
            existingProduct.setCategory(categoryOpt.get());
        } else {
            throw new ProductNotFoundException("Category not found");
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())// Adiciona o ID da categoria
                .build();
    }
}
