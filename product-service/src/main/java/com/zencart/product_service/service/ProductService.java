package com.zencart.product_service.service;

import com.zencart.product_service.dto.ProductDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProductService {

     List<ProductDto> findAll();

     ProductDto findById(final Integer productId);

     ProductDto save(final ProductDto productDto);

     ProductDto update(final ProductDto productDto);

     ProductDto update(final ProductDto productDto, final Integer productId);

    void deleteById(final Integer productId);
}
