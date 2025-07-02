package com.smartspending.transaction.repository;

import com.smartspending.common.response.PageInfoResponseDto;
import com.smartspending.dashboard.dto.response.CategoryStatisticDto;
import com.smartspending.dashboard.dto.response.IncomeExpenseChartDto;
import com.smartspending.dashboard.dto.response.TransactionSummaryDto;
import com.smartspending.transaction.dto.response.TransactionResponseDto;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryCustom {

    PageInfoResponseDto<TransactionResponseDto> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to, Pageable pageable);

    Optional<BigDecimal> sumIncomeByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate);

    Optional<BigDecimal> sumExpenseByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate);

    Optional<List<CategoryStatisticDto>> findTopSpendingCategories(Long userId, LocalDate startDate, LocalDate endDate);

    List<IncomeExpenseChartDto> getIncomeExpenseChart(Long userId, LocalDate start, LocalDate end);

    List<TransactionSummaryDto> findRecentTransactions(Long userId, int limit);


}
