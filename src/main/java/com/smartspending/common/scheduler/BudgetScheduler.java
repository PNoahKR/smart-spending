package com.smartspending.common.scheduler;

import com.smartspending.budget.entity.Budget;
import com.smartspending.budget.repository.BudgetRepository;
import com.smartspending.budget.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BudgetScheduler {

    private final BudgetRepository budgetRepository;
    private final BudgetService budgetService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void renewBudgetScheduler() {
        List<Budget> activeBudget = budgetRepository.findByIsActiveTrueAndIsRecurringTrue();

        for (Budget budget : activeBudget) {
            if (budget.getEndDate().isBefore(LocalDate.now())) {
                budgetService.renewBudget(budget.getId());
            }
        }
    }
}
