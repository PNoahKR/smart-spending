package com.smartspending.budget.dto.response;

import com.smartspending.budget.entity.Budget;
import com.smartspending.budget.enums.BudgetPeriodType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponseDto {

    private Long id;
    private BigDecimal amount;
    private BudgetPeriodType periodType;
    private Integer period;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private boolean isRecurring;

    public BudgetResponseDto(Budget budget) {
        this.id = budget.getId();
        this.amount = budget.getAmount();
        this.periodType = budget.getPeriodType();
        this.period = budget.getPeriod();
        this.startDate = budget.getStartDate();
        this.endDate = budget.getEndDate();
        this.isActive = budget.isActive();
        this.isRecurring = budget.isRecurring();
    }
}
