package com.smartspending.category.service;

import com.smartspending.category.dto.request.CategoryRequestDto;
import com.smartspending.category.dto.request.CategoryUpdateDto;
import com.smartspending.category.dto.response.CategoryResponseDto;
import com.smartspending.category.entity.Category;
import com.smartspending.category.repository.CategoryRepository;
import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import com.smartspending.user.entity.User;
import com.smartspending.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<CategoryResponseDto> getList(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void create(CategoryRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(CommonResponseCode.USER_NOT_FOUND));
        String categoryName = requestDto.getName();
        validateCategoryName(categoryName);
        Category category = Category.builder()
                .name(categoryName)
                .user(user)
                .isDefault(false)
                .build();
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void update(CategoryUpdateDto requestDto, Long userId) {
        Long categoryId = requestDto.getId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomException(CommonResponseCode.CATEGORY_NOT_FOUND));

        validateUserId(userId, category);

        String newCategoryName = requestDto.getName();
        validateCategoryName(newCategoryName);

        category.updateName(newCategoryName);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CustomException(CommonResponseCode.CATEGORY_NOT_FOUND));
        validateUserId(userId, category);
        categoryRepository.deleteById(id);
    }

    private static void validateUserId(Long userId, Category category) {
        if (!category.getUser().getId().equals(userId)) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }
    }

    private void validateCategoryName(String categoryName) {
        if (categoryRepository.findByName(categoryName).isPresent()) {
            throw new CustomException(CommonResponseCode.CATEGORY_ALREADY_EXISTS);
        }
    }
}
