package com.restaurant_management_app.rma.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String productNotFound) {
        super(productNotFound);
    }
}
