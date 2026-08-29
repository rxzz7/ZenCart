package com.zencart.product_service.service;

import com.zencart.product_service.dto.CategoryDto;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();
    CategoryDto findById(Integer categoryId);
    CategoryDto save(CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto);
    CategoryDto update(CategoryDto categoryDto,Integer categoryId);
    void deleteById(Integer categoryId);

    Page<CategoryDto> findAllCategories(int page, int size);
    List<CategoryDto> getAllCategories(int page, int size, String sortBy);
}
