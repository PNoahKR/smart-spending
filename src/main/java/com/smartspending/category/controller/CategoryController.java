package com.smartspending.category.controller;

import com.smartspending.category.dto.request.CategoryRequestDto;
import com.smartspending.category.dto.request.CategoryUpdateDto;
import com.smartspending.category.service.CategoryService;
import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public CommonResponse<Void> createCategory(@RequestBody CategoryRequestDto categoryRequestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoryService.create(categoryRequestDto, userDetails.getUserId());
        return ApiResponseUtil.success();
    }

    @PostMapping("/update")
    public CommonResponse<Void> updateCategory(@RequestBody CategoryUpdateDto categoryUpdateDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoryService.update(categoryUpdateDto, userDetails.getUserId());
        return ApiResponseUtil.success();
    }

    @DeleteMapping("/delete")
    public CommonResponse<Void> deleteCategory(@RequestParam Long categoryId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoryService.delete(categoryId, userDetails.getUserId());
        return ApiResponseUtil.success();
    }
}
