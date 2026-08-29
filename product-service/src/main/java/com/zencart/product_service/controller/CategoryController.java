package com.zencart.product_service.controller;

import com.zencart.product_service.dto.CategoryDto;
import com.zencart.product_service.dto.ProductDto;
import com.zencart.product_service.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll(){
        log.info("CategoryDto List, controller; fetch all categories");
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<CategoryDto>> findAllCategories(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size){
        return new ResponseEntity<>(categoryService.findAllCategories(page, size), HttpStatus.OK);

    }
    @GetMapping("/paging-and-sorting")
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "categoryId") String sortBy){
        return new ResponseEntity<>(categoryService.getAllCategories(page, size, sortBy), new HttpHeaders(), HttpStatus.OK);

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> findById(@PathVariable(name = "categoryId")
                                                    @NotBlank(message = "Input must not be blank")
                                                    @Valid final String categoryId){
        log.info("CategoryDto, controller; fetch category by id ");
        return ResponseEntity.ok(categoryService.findById(Integer.parseInt(String.format(categoryId).strip())));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> save(@RequestBody
                                            @NotNull(message = "Input must not be null")
                                            @Valid final CategoryDto categoryDto){
        log.info("CategoryDto, controller; save Category");
        return ResponseEntity.ok(categoryService.save(categoryDto));
    }

    @PutMapping
    public  ResponseEntity<CategoryDto> update(@RequestBody
                                               @NotNull(message = "Input must not be null")
                                               @Valid final CategoryDto categoryDto){
        log.info("CategoryDto, controller; update category");
        return ResponseEntity.ok(categoryService.update(categoryDto));
    }

    @PutMapping("/{categoryId}")
    public  ResponseEntity<CategoryDto> update(@RequestBody
                                               @NotNull(message = "Input must not be null")
                                               @Valid final CategoryDto categoryDto,
                                               @PathVariable(name = "categoryId")
                                               @NotBlank(message = "Input must not be blank ")
                                               @Valid final String categoryId){
        log.info("CategoryDto, controller; update category by id ");
        return ResponseEntity.ok(categoryService.update(categoryDto, Integer.parseInt(String.format(categoryId).strip())));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable(name = "categoryId")
                                              @NotBlank(message = "Input must not be blank")
                                              @Valid final String categoryId){
        log.info("Void, controller; delete by by id ");
        categoryService.deleteById(Integer.parseInt(String.format(categoryId).strip()));
        return ResponseEntity.ok(true);
    }

}
