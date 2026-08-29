package com.zencart.product_service.controller;


import com.zencart.product_service.dto.ProductDto;
import com.zencart.product_service.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll(){
        log.info("ProductDto List, controller; fetch all products");
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findById(@PathVariable(name = "productId")
                                                   @NotBlank(message = "Input must not be blank")
                                                   @Valid final String productId){
        log.info("ProductDto, resource; fetch product by id");
        return ResponseEntity.ok(productService.findById(Integer.parseInt(String.format(productId).strip())));
    }

    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody
                                               @NotNull(message = "Input must not be NULL")
                                                @Valid ProductDto productDto){
        log.info("ProductDto, resource, save product");
        return ResponseEntity.ok(productService.save(productDto));
    }

    @PutMapping
    public ResponseEntity<ProductDto> update(@RequestBody
                                             @NotNull(message = "Input must not be NULL")
                                             @Valid final ProductDto productDto){
        log.info("ProductDto, resource; update product");
        return ResponseEntity.ok(productService.update(productDto));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@RequestBody
                                             @NotNull(message = "Input must not be NULL")
                                             @Valid final ProductDto productDto,
                                             @PathVariable(name = "productId")
                                             @NotBlank(message = "Input must not be blank")
                                             @Valid final String productId){
        log.info("ProductDto, resource; update product by id");
        return ResponseEntity.ok(productService.update(productDto, Integer.parseInt(String.format(productId).strip())));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable(name = "productId")
                                              @NotBlank(message = "Input must not be blank")
                                              @Valid final String productId){
        log.info("ProductDto, resource; delete product by id");
        productService.deleteById(Integer.parseInt(String.format(productId).strip()));
        return ResponseEntity.ok(true);
    }
}
