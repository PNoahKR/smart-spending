package com.smartspending.category.dto.response;

import com.smartspending.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String name;

    public static CategoryResponseDto from(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName());
    }
}
