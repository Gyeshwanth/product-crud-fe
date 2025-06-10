package com.yeshwanth.pmca.repository;

import com.yeshwanth.pmca.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}