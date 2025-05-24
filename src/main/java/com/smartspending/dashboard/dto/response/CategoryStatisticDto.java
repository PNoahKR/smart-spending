package com.smartspending.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryStatisticDto {
    private String categoryName;
    private BigDecimal amount;
}
