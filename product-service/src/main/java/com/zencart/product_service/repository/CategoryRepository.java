package com.zencart.product_service.repository;

import com.zencart.product_service.entity.Category;
import com.zencart.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAll(Pageable pageable);
}
