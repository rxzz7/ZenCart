package com.zencart.product_service.service.impl;

import com.zencart.product_service.dto.ProductDto;
import com.zencart.product_service.entity.Product;
import com.zencart.product_service.exception.ProductNotFoundException;
import com.zencart.product_service.mapper.CategoryMapper;
import com.zencart.product_service.mapper.ProductMapper;
import com.zencart.product_service.repository.ProductRepository;
import com.zencart.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> findAll() {

        return productRepository.findAll().stream()
                .map(product -> ProductMapper.map(product))
                .distinct()
                .toList();
    }

    @Override
    public ProductDto findById(Integer productId) {
        return productRepository.findById(productId).map(ProductMapper::map)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with this Id"));
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        log.info("ProductDto, service; save product");
        try {
            return ProductMapper.map(productRepository.save(ProductMapper.map(productDto)));
        }
        catch (DataIntegrityViolationException ex){
            log.error("Error saving product: Data integrity violation", ex);
            throw new ProductNotFoundException("Error saving product: Data integrity violation", ex);
        }
        catch (Exception ex){
            log.info("Error saving product");
            throw new ProductNotFoundException("Error saving product", ex);
        }
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        log.info("ProductDto, service; update product");
        Product existingProduct = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with this Id"));
        BeanUtils.copyProperties(productDto, existingProduct, "ProductId", "categoryDto");
        if(productDto.getCategoryDto() != null){
            existingProduct.setCategory(CategoryMapper.map(productDto.getCategoryDto()));
        }
        return ProductMapper.map(productRepository.save(existingProduct));
    }

    @Override
    public ProductDto update(ProductDto productDto, Integer productId) {
        log.info("ProductDto, service; update product by ID");
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with this Id"));
        BeanUtils.copyProperties(productDto, existingProduct, "ProductId", "categoryDto");
        if(productDto.getCategoryDto() != null){
            existingProduct.setCategory(CategoryMapper.map(productDto.getCategoryDto()));
        }
        return ProductMapper.map(productRepository.save(existingProduct));
    }

    @Override
    public void deleteById(Integer productId) {
        log.info("Void, service; delete product by id ");
        productRepository.delete(ProductMapper.map(findById(productId)));
    }
}
