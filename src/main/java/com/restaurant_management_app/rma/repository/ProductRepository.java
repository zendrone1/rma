package com.restaurant_management_app.rma.repository;

import com.restaurant_management_app.rma.model.Category;
import com.restaurant_management_app.rma.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByName(String name);
}
