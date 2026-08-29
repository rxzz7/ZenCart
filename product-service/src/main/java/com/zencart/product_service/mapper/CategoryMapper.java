package com.zencart.product_service.mapper;

import com.zencart.product_service.dto.CategoryDto;
import com.zencart.product_service.entity.Category;

import java.util.Optional;

public interface CategoryMapper {

    static Category map(CategoryDto categoryDto){

        final var parentCategoryDto = Optional.ofNullable(categoryDto.getParentCategoryDto())
                .orElseGet(CategoryDto::new);

        Category parentCategory = Category.builder()
                .categoryId(parentCategoryDto.getCategoryId())
                .categoryTitle(parentCategoryDto.getCategoryTitle())
                .imageUrl(parentCategoryDto.getImageUrl())
                .build();

        return Category.builder()
                .categoryId(categoryDto.getCategoryId())
                .categoryTitle(categoryDto.getCategoryTitle())
                .imageUrl(categoryDto.getImageUrl())
                .parentCategory(parentCategory)
                .build();
    }

    static CategoryDto map(Category category){
        final var parentCategory = Optional.ofNullable(category.getParentCategory())
                .orElseGet(Category::new);

        CategoryDto parentCategoryDto = CategoryDto.builder()
                .categoryId(parentCategory.getCategoryId())
                .categoryTitle(parentCategory.getCategoryTitle())
                .imageUrl(parentCategory.getImageUrl())
                .build();

        return CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryTitle(category.getCategoryTitle())
                .imageUrl(category.getImageUrl())
                .parentCategoryDto(parentCategoryDto)
                .build();
    }

    public static Category convertToEntity(CategoryDto categoryDTO) {
        Category category = new Category();
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setCategoryTitle(categoryDTO.getCategoryTitle());
        category.setImageUrl(categoryDTO.getImageUrl());
        // You may handle subcategories and products similarly if needed

        return category;
    }
    public static CategoryDto convertToDTO(Category category) {
        CategoryDto categoryDTO = new CategoryDto();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryTitle(category.getCategoryTitle());
        categoryDTO.setImageUrl(category.getImageUrl());
        // You may handle subcategories and products similarly if needed

        return categoryDTO;
    }

}
