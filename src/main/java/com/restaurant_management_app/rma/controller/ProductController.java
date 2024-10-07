package com.restaurant_management_app.rma.controller;

import com.restaurant_management_app.rma.model.Product;
import com.restaurant_management_app.rma.response.ApiResponse;
import com.restaurant_management_app.rma.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")

public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity <ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity.ok(newApiresponse("Success"));

    }
}
