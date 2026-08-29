package com.zencart.product_service.service.impl;

import com.zencart.product_service.dto.CategoryDto;
import com.zencart.product_service.entity.Category;
import com.zencart.product_service.exception.CategoryNotFoundException;
import com.zencart.product_service.mapper.CategoryMapper;
import com.zencart.product_service.repository.CategoryRepositoryPagingAndSorting;
import com.zencart.product_service.repository.CategoryRepository;
import com.zencart.product_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.zencart.product_service.mapper.CategoryMapper.convertToDTO;
import static com.zencart.product_service.mapper.CategoryMapper.convertToEntity;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryRepositoryPagingAndSorting categoryRepositoryPagingAndSorting;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDto> findAll() {
        log.info("CategoryDto List, Service; fetching all categories");
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::map)
                .distinct()
                .toList();
    }

    @Override
    public CategoryDto findById(Integer categoryId) {
        log.info("CategoryDto , Service ; fetching category by id");
        return CategoryMapper.map(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.format("Category not found with id %d", categoryId))));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        log.info("CategoryDto, service; save category");
        // Convert DTO to Entity
        Category category = convertToEntity(categoryDto);

        // If the parent category is provided in the DTO, find it in the database
        if (categoryDto.getParentCategoryDto() != null && categoryDto.getParentCategoryDto().getCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDto.getParentCategoryDto().getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parentCategory);
        }

        // Save the category entity
        Category savedCategory = this.categoryRepository.save(category);

        // Convert the saved entity back to DTO and return
        return convertToDTO(savedCategory);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        log.info("CategoryDto, Service; update category");

        try {
            Category existingCategory = categoryRepository.findById(categoryDto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id "+ categoryDto.getCategoryId()));
            BeanUtils.copyProperties(categoryDto, existingCategory, "categoryId", "parentCategoryDto");
            if(categoryDto.getParentCategoryDto() != null){
                existingCategory.setParentCategory(CategoryMapper.map(categoryDto.getParentCategoryDto()));
            }
            return CategoryMapper.map(categoryRepository.save(existingCategory));
        }  catch (CategoryNotFoundException e) {
            log.error("Error updating category. Category with id [{}] not found.", categoryDto.getCategoryId());
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("Error updating category: Data integrity violation", e);
            throw new CategoryNotFoundException("Error updating category: Data integrity violation", e);
        } catch (Exception e) {
            log.error("Error updating category", e);
            throw new CategoryNotFoundException("Error updating category", e);
        }

    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, Integer categoryId) {
        log.info("CategoryDto, Service; update category by id");

        try {
            CategoryDto category = findById(categoryId);
            Category existingCategory = CategoryMapper.map(category);
            BeanUtils.copyProperties(categoryDto, existingCategory, "categoryId", "parentCategoryDto");
            if(categoryDto.getParentCategoryDto() != null){
                existingCategory.setParentCategory(CategoryMapper.map(categoryDto.getParentCategoryDto()));
            }
            return CategoryMapper.map(categoryRepository.save(existingCategory));
        }catch (CategoryNotFoundException e) {
            log.error("Error updating category. Category with id [{}] not found .", categoryId);
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("Error updating category: Data integrity violation", e);
            throw new CategoryNotFoundException("Error updating category: Data integrity violation", e);
        } catch (Exception e) {
            log.error("Error updating category", e);
            throw new CategoryNotFoundException("Error updating category", e);
        }
    }

    @Override
    public void deleteById(Integer categoryId) {
        log.info("Void Service ; Delete category by id");
        try{
            categoryRepository.deleteById(categoryId);
        }catch (Exception e){
            throw new CategoryNotFoundException("Error deleting category", e);
        }
    }

    @Override
    public Page<CategoryDto> findAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryDto> categoryDtos = categoryPage.getContent()
                .stream().map(CategoryMapper::map)
                .distinct()
                .toList();
        return new PageImpl<>(categoryDtos, pageable, categoryPage.getTotalElements());
        //A List doesn't contain pagination metadata. so have to construct a new page by PageImpl<>()
    }

    @Override
    public List<CategoryDto> getAllCategories(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Category> pagedResult = categoryRepositoryPagingAndSorting.findAllPagedAndSortedCategories(pageable);
        if(pagedResult.hasContent()){
            return pagedResult.getContent()
                    .stream()
                    .map(element -> modelMapper.map(pagedResult, CategoryDto.class))
                    .toList();
        }
        return new ArrayList<>();
    }
}
