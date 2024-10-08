package com.restaurant_management_app.rma.controller;

import com.restaurant_management_app.rma.dto.ProductDTO;
import com.restaurant_management_app.rma.model.Product;
import com.restaurant_management_app.rma.request.AddProductRequest;
import com.restaurant_management_app.rma.response.ApiResponse;
import com.restaurant_management_app.rma.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")

public class ProductController {
    @Autowired
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity <ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Sucess",products));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request) {
        try {
            Product product = productService.addProduct(request);
            ProductDTO productDto = productService.convertToDTO(product);
            return ResponseEntity.ok(new ApiResponse("Product added successfully!", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
