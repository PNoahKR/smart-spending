package com.smartspending.category.service;

import com.smartspending.category.dto.request.CategoryRequestDto;
import com.smartspending.category.dto.request.CategoryUpdateDto;
import com.smartspending.category.dto.response.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> getList(Long userId);
    void create(CategoryRequestDto requestDto, Long userId);
    void update(CategoryUpdateDto requestDto, Long userId);
    void delete(Long id, Long userId);
}
