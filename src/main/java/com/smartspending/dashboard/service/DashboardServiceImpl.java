package com.smartspending.dashboard.service;

import com.smartspending.budget.entity.Budget;
import com.smartspending.budget.enums.BudgetPeriodType;
import com.smartspending.budget.repository.BudgetRepository;
import com.smartspending.dashboard.dto.response.*;
import com.smartspending.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    @Override
    public DashboardResponseDto getDashboard(Long userId, LocalDate startDate, LocalDate endDate) {
        // 1. 기본 조회 기간: 이번 달
        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        // 2. 수입/지출 총합
        BigDecimal totalIncome = transactionRepository
                .sumIncomeByUserAndDate(userId, startDate, endDate)
                .orElse(BigDecimal.ZERO);

        BigDecimal totalExpense = transactionRepository
                .sumExpenseByUserAndDate(userId, startDate, endDate)
                .orElse(BigDecimal.ZERO);

        // 3. 예산 조회 및 예산 기간 내 지출 계산
        BigDecimal mainBudgetAmount = BigDecimal.ZERO;
        BigDecimal remainingBudget = BigDecimal.ZERO;

        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndMainBudgetTrue(userId);
        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();
            mainBudgetAmount = budget.getAmount();

            // 예산 종료일 계산
            LocalDate budgetStart = budget.getStartDate();
            LocalDate budgetEnd;
            if (budget.getPeriodType() == BudgetPeriodType.MONTHLY) {
                budgetEnd = budgetStart.plusMonths(budget.getPeriod()).minusDays(1);
            } else if (budget.getPeriodType() == BudgetPeriodType.WEEKLY) {
                budgetEnd = budgetStart.plusWeeks(budget.getPeriod()).minusDays(1);
            } else {
                budgetEnd = budgetStart; // fallback
            }

            // 예산 기간 내 실제 지출
            BigDecimal expenseInBudgetPeriod = transactionRepository
                    .sumExpenseByUserAndDate(userId, budgetStart, budgetEnd)
                    .orElse(BigDecimal.ZERO);

            remainingBudget = mainBudgetAmount.subtract(expenseInBudgetPeriod);
        }

        // 4. 수입/지출 비율
        BigDecimal totalAmount = totalIncome.add(totalExpense);
        BigDecimal incomePercentage = BigDecimal.ZERO;
        BigDecimal expensePercentage = BigDecimal.ZERO;
        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            incomePercentage = totalIncome.divide(totalAmount, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            expensePercentage = totalExpense.divide(totalAmount, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        // 5. 요약 정보
        SummaryDto summary = new SummaryDto(
                totalIncome,
                totalExpense,
                mainBudgetAmount,
                remainingBudget,
                incomePercentage,
                expensePercentage
        );

        // 6. 차트 및 카테고리 지출
        List<IncomeExpenseChartDto> incomeExpenseByDate =
                transactionRepository.getIncomeExpenseChart(userId, startDate, endDate);

        List<CategoryStatisticDto> categoryStats =
                transactionRepository.findTopSpendingCategories(userId, startDate, endDate).orElse(Collections.emptyList());

        List<TransactionSummaryDto> recentTransactions =
                transactionRepository.findRecentTransactions(userId, 5);

        return new DashboardResponseDto(
                incomeExpenseByDate,
                categoryStats,
                summary,
                recentTransactions
        );
    }
}
