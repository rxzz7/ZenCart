package com.zencart.product_service.mapper;

import com.zencart.product_service.dto.CategoryDto;
import com.zencart.product_service.dto.ProductDto;
import com.zencart.product_service.entity.Category;
import com.zencart.product_service.entity.Product;

public interface ProductMapper {


    static ProductDto map(final Product product){

        CategoryDto categoryDto = CategoryDto.builder()
                .categoryId(product.getCategory().getCategoryId())
                .categoryTitle(product.getCategory().getCategoryTitle())
                .imageUrl(product.getCategory().getImageUrl())
                .build();

        return ProductDto.builder()
                .productId(product.getProductId())
                .productTitle(product.getProductTitle())
                .imageUrl(product.getImageUrl())
                .sku(product.getSku())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryDto(categoryDto)
                .build();

    }

    static Product map(final ProductDto productDto){

        Category category = Category.builder()
                .categoryId(productDto.getCategoryDto().getCategoryId())
                .categoryTitle(productDto.getCategoryDto().getCategoryTitle())
                .imageUrl(productDto.getCategoryDto().getImageUrl())
                .build();

        return Product.builder()
                .productId(productDto.getProductId())
                .productTitle(productDto.getProductTitle())
                .imageUrl(productDto.getImageUrl())
                .sku(productDto.getSku())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .category(category)
                .build();
    }
}
