package com.smartspending.budget.repository;

import com.smartspending.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long>, BudgetRepositoryCustom {
    Optional<Budget> findByMainBudgetTrueAndUserId(Long userId);
}
