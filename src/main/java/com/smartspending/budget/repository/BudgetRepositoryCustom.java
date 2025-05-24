package com.smartspending.budget.repository;

import com.smartspending.budget.entity.Budget;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BudgetRepositoryCustom {

    List<Budget> findByIsActiveTrueAndIsRecurringTrue();
    Optional<BigDecimal> findMainBudgetByUser (Long userId);
}
