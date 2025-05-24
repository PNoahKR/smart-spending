package com.smartspending.budget.entity;

import com.smartspending.budget.enums.BudgetPeriodType;
import com.smartspending.common.entity.BaseEntity;
import com.smartspending.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "budget")
public class Budget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private BudgetPeriodType periodType;

    @Column(name = "period", nullable = false)
    private Integer period;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring;

    @Column(name = "main_budget", nullable = false)
    private boolean mainBudget;

    @Builder
    public Budget(User user, BigDecimal amount, BudgetPeriodType periodType, Integer period, LocalDate startDate, LocalDate endDate, boolean isActive, boolean isRecurring, boolean mainBudget) {
        this.user = user;
        this.amount = amount;
        this.periodType = periodType;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.isRecurring = isRecurring;
        this.mainBudget = mainBudget;
    }

    public void updateStartDate(LocalDate newStartDate) {
        this.startDate = newStartDate;
    }

    public void updateEndDate(LocalDate newEndDate) {
        this.endDate = newEndDate;
    }

    public void updateAmount(BigDecimal newAmount) {
        this.amount = newAmount;
    }

    public void updatePeriodType(BudgetPeriodType newPeriodType) {
        this.periodType = newPeriodType;
    }

    public void updatePeriod(Integer newPeriod) {
        this.period = newPeriod;
    }

    public void updateIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void updateIsRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public void updateMainBudget(boolean mainBudget) {
        this.mainBudget = mainBudget;
    }
}
