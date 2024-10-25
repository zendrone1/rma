package com.restaurant_management_app.rma.service.product;

import com.restaurant_management_app.rma.dto.ProductDTO;
import com.restaurant_management_app.rma.exceptions.ProductNotFoundException;
import com.restaurant_management_app.rma.model.Category;
import com.restaurant_management_app.rma.model.Product;
import com.restaurant_management_app.rma.repository.CategoryRepository;
import com.restaurant_management_app.rma.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    class getAll {

        @Test
        @DisplayName("Should return all products")
        void shouldReturnAllProducts() {
            //Arrange
            Category category = new Category("Test Category");
            category.setId(1L);

        List<Product> products = Arrays.asList(
                new Product("Test Product",new BigDecimal("10.6"),2,category),
                new Product("Test Product 2",new BigDecimal("25.6"),10,category)
        );

        when(productRepository.findAll()).thenReturn(products);

        //Act
            List<ProductDTO> productDTOS = productService.getAllProducts();


         //Assert
         assertNotNull(productDTOS);
         assertEquals(2,productDTOS.size());

        // Verifying the first product
        ProductDTO productDTO1 = productDTOS.get(0);
        assertEquals("Test Product", productDTO1.getName());
        assertEquals(new BigDecimal("10.6"), productDTO1.getPrice());
        assertEquals(2, productDTO1.getQuantity());
        assertEquals(1L, productDTO1.getCategoryId());

        // Verifying the second product
        ProductDTO productDTO2 = productDTOS.get(1);
        assertEquals("Test Product 2", productDTO2.getName());
        assertEquals(new BigDecimal("25.6"), productDTO2.getPrice());
        assertEquals(10, productDTO2.getQuantity());
        assertEquals(1L, productDTO2.getCategoryId());

        // Verifying that the findAll method was called once
        verify(productRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return an empty list when there are no products")
        void shouldReturnEmptyListWhenNoProducts() {
            // Arrange: Simula que o repositório retorna uma lista vazia
            when(productRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<ProductDTO> products = productService.getAllProducts();

            // Assert
            assertNotNull(products);
            assertTrue(products.isEmpty());

            // Verifica que o método findAll foi chamado uma vez
            verify(productRepository, times(1)).findAll();
        }
    }

    @Nested
    class GetProductById {

        @Test
        @DisplayName("Should return product by ID when product exists")
        void shouldReturnProductByIdWhenProductExists() {
            // Arrange
            Category category = new Category("Test Category");
            category.setId(1L);

            Product product = new Product("Test Product", new BigDecimal("10.0"), 2, category);
            product.setId(1L);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            // Act
            ProductDTO productDTO = productService.getProductById(1L);

            // Assert
            assertNotNull(productDTO);
            assertEquals("Test Product", productDTO.getName());
            assertEquals(new BigDecimal("10.0"), productDTO.getPrice());
            assertEquals(2, productDTO.getQuantity());
            assertEquals(1L, productDTO.getCategoryId());

            verify(productRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw ProductNotFoundException when product is not found")
        void shouldThrowProductNotFoundExceptionWhenProductIsNotFound() {
            // Arrange
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
                productService.getProductById(1L);
            });

            assertEquals("Product not found", exception.getMessage());
            verify(productRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class CreateProduct {

        @Test
        @DisplayName("Should create a product with an existing category")
        void shouldCreateProductWithExistingCategory() {
            // Arrange
            Category existingCategory = new Category("Test Category");
            existingCategory.setId(1L);

            // Mocking the category repository to return the existing category
            when(categoryRepository.findById(existingCategory.getId())).thenReturn(Optional.of(existingCategory));

            // Create a ProductDTO to be used for adding a new product
            ProductDTO productDTO = ProductDTO.builder()
                    .name("Test Product")
                    .price(new BigDecimal("10.6"))
                    .quantity(2)
                    .categoryId(existingCategory.getId())
                    .build();

            // Create a Product object that should be returned from the save method
            Product savedProduct = new Product("Test Product", new BigDecimal("10.6"), 2, existingCategory);
            savedProduct.setId(1L); // Simulando um ID gerado para o produto salvo

            // Mocking the product repository to return the saved product
            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            // Act
            ProductDTO createdProductDTO = productService.addProduct(productDTO);

            // Assert
            assertNotNull(createdProductDTO);
            assertEquals("Test Product", createdProductDTO.getName());
            assertEquals(new BigDecimal("10.6"), createdProductDTO.getPrice());
            assertEquals(2, createdProductDTO.getQuantity());
            assertEquals(existingCategory.getId(), createdProductDTO.getCategoryId());

            // Verifica se o método save foi chamado uma vez
            verify(productRepository, times(1)).save(any(Product.class));
        }

        @Test
        @DisplayName("Should create a product with a new category")
        void shouldCreateProductWithNewCategory() {
            // Arrange
            ProductDTO productDTO = ProductDTO.builder()
                    .name("New Product")
                    .price(new BigDecimal("15.0"))
                    .quantity(5)
                    .categoryName("New Category")
                    .build();

            // Mocking the category repository to return an empty Optional (new category)
            when(categoryRepository.findByName(productDTO.getCategoryName())).thenReturn(Optional.empty());

            // Create a new category and mock its save
            Category newCategory = new Category("New Category");
            when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

            // Create a Product object that should be returned from the save method
            Product savedProduct = new Product("New Product", new BigDecimal("15.0"), 5, newCategory);
            savedProduct.setId(2L); // Simulando um ID gerado para o produto salvo

            // Mocking the product repository to return the saved product
            when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

            // Act
            ProductDTO createdProductDTO = productService.addProduct(productDTO);

            // Assert
            assertNotNull(createdProductDTO);
            assertEquals("New Product", createdProductDTO.getName());
            assertEquals(new BigDecimal("15.0"), createdProductDTO.getPrice());
            assertEquals(5, createdProductDTO.getQuantity());
            assertEquals("New Category", createdProductDTO.getCategoryName());

            // Verifica se o método save foi chamado uma vez
            verify(productRepository, times(1)).save(any(Product.class));
            verify(categoryRepository, times(1)).save(any(Category.class));
        }

        @Test
        @DisplayName("Should throw exception if product has invalid price or quantity")
        void shouldThrowExceptionIfInvalidPriceOrQuantity() {
            // Arrange
            ProductDTO productDTO = ProductDTO.builder()
                    .name("Invalid Product")
                    .price(new BigDecimal("-5.0"))  // Invalid price
                    .quantity(-1)  // Invalid quantity
                    .categoryId(1L)
                    .build();

            // Act & Assert
            Exception exception = assertThrows(ProductNotFoundException.class, () -> productService.addProduct(productDTO));
            assertEquals("Category not found with the given ID.", exception.getMessage());

            verify(productRepository, never()).save(any(Product.class));
        }
    }

    @Nested
    class DeleteProductById {
        @Test
        @DisplayName("Should delete product when product exists")
        void shouldDeleteProductWhenExist() {
            //Arrange
            Long productId = 1L;
            Product product = new Product();
            product.setId(productId);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            //Act
            productService.deleteProductById(productId);

            //Assert
            verify(productRepository, times(1)).delete(product);
            verify(productRepository, times(1)).findById(productId);
        }

        @Test
        @DisplayName("Should throw exception when product does not exist")
        void shouldThrowExceptionWhenProductDoesNotExist() {
            // Arrange
            Long productId = 1L;
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // Act & Assert
            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productService.deleteProductById(productId)
            );
            assertEquals("Product not found", exception.getMessage());

            verify(productRepository, times(1)).findById(productId);
            verify(productRepository, never()).delete(any());
        }
    }
}
