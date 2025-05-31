package com.smartspending.dashboard.service;

import com.smartspending.budget.repository.BudgetRepository;
import com.smartspending.dashboard.dto.response.CategoryStatisticDto;
import com.smartspending.dashboard.dto.response.DashboardResponseDto;
import com.smartspending.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    @Override
    public DashboardResponseDto getDashboard(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        BigDecimal totalIncome = transactionRepository.sumIncomeByUserAndDate(userId, startDate, endDate).orElse(BigDecimal.ZERO);
        BigDecimal totalExpense = transactionRepository.sumExpenseByUserAndDate(userId, startDate, endDate).orElse(BigDecimal.ZERO);

        BigDecimal mainBudget = budgetRepository.findMainBudgetByUser(userId).orElse(new BigDecimal(0));
        BigDecimal remainingBudget = mainBudget.subtract(totalExpense);

        List<CategoryStatisticDto> topCategories = transactionRepository.findTopSpendingCategories(userId, startDate, endDate).orElse(Collections.emptyList());

        BigDecimal totalAmount = totalIncome.add(totalExpense); // 총 거래 금액
        BigDecimal incomePercentage = BigDecimal.ZERO;
        BigDecimal expensePercentage = BigDecimal.ZERO;

        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            incomePercentage = totalIncome.divide(totalAmount, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            expensePercentage = totalExpense.divide(totalAmount, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }

        return new DashboardResponseDto(totalIncome, totalExpense, mainBudget, remainingBudget, incomePercentage, expensePercentage, topCategories);
    }
}
