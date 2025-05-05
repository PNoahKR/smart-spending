package com.smartspending.budget.repository;

import com.smartspending.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByIsActiveTrueAndIsRecurringTrue();
}
