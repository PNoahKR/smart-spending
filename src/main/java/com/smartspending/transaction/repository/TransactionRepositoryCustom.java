package com.smartspending.transaction.repository;

import com.smartspending.dashboard.dto.response.CategoryStatisticDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryCustom {

    Optional<BigDecimal> sumIncomeByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate);

    Optional<BigDecimal> sumExpenseByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate);

    Optional<List<CategoryStatisticDto>> findTopSpendingCategories(Long userId, LocalDate startDate, LocalDate endDate);

}
