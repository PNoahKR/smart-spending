package com.smartspending.transaction.repository;

import com.smartspending.dashboard.dto.response.CategoryStatisticDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepositoryCustom {

    BigDecimal sumIncomeByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumExpenseByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate);

    List<CategoryStatisticDto> findTopSpendingCategories(Long userId, LocalDate startDate, LocalDate endDate);

}
