package com.smartspending.budget.service;

import com.smartspending.budget.dto.request.BudgetRequestDto;
import com.smartspending.budget.dto.response.BudgetResponseDto;
import com.smartspending.budget.entity.Budget;

import java.util.List;

public interface BudgetService {

    List<BudgetResponseDto> getBudgets(Long userId);

    BudgetResponseDto create(BudgetRequestDto requestDto, Long userId);

    BudgetResponseDto update(Long id, BudgetRequestDto requestDto, Long userId);

    void delete(Long id, Long userId);

    Budget renewBudget(Long budgetId);
}
