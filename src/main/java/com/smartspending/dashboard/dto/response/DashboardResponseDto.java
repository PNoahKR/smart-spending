package com.smartspending.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;

    private BigDecimal mainBudget;
    private BigDecimal remainingBudget;

    private BigDecimal incomePercentage;
    private BigDecimal expensePercentage;

    private List<CategoryStatisticDto> topCategories;
}
