package com.smartspending.category.service;

import com.smartspending.category.dto.request.CategoryRequestDto;
import com.smartspending.category.dto.request.CategoryUpdateDto;

public interface CategoryService {
    void create(CategoryRequestDto requestDto, Long userId);
    void update(CategoryUpdateDto requestDto, Long userId);
    void delete(Long id, Long userId);
}
