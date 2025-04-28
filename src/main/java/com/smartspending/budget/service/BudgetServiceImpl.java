package com.smartspending.budget.service;

import com.smartspending.budget.dto.request.BudgetRequestDto;
import com.smartspending.budget.dto.response.BudgetResponseDto;
import com.smartspending.budget.entity.Budget;
import com.smartspending.budget.enums.BudgetPeriodType;
import com.smartspending.budget.repository.BudgetRepository;
import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import com.smartspending.user.entity.User;
import com.smartspending.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BudgetResponseDto create(BudgetRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CommonResponseCode.USER_NOT_FOUND));

        BudgetPeriodType periodType = requestDto.getPeriodType();
        Integer period = requestDto.getPeriod();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate;
        if (periodType.equals(BudgetPeriodType.MONTHLY)) {
            if (period != null) {
                endDate = startDate.plusMonths(period).minusDays(1);
            } else {
                throw new CustomException(CommonResponseCode.NULL_INPUT);
            }
        } else if (periodType.equals(BudgetPeriodType.WEEKLY)) {
            if (period != null) {
                endDate = startDate.plusWeeks(period).minusDays(1);
            } else {
                throw new CustomException(CommonResponseCode.NULL_INPUT);
            }
        } else {
            throw new CustomException(CommonResponseCode.NULL_INPUT);
        }

        boolean isActive = requestDto.isActive();
        boolean isRecurring = requestDto.isRecurring();

        Budget budget = Budget.builder()
                .user(user)
                .amount(requestDto.getAmount())
                .periodType(periodType)
                .period(period)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(isActive)
                .isRecurring(isRecurring)
                .build();

        budgetRepository.save(budget);

        return new BudgetResponseDto(budget);
    }

    @Override
    @Transactional
    public Budget renewBudget(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new CustomException(CommonResponseCode.BUDGET_NOT_FOUND));

        if (!budget.isActive()) {
            throw new CustomException(CommonResponseCode.IS_NOT_ACTIVE);
        } else if (!budget.isRecurring()) {
            throw new CustomException(CommonResponseCode.IS_NOT_RECURRING);
        }

        if (budget.getPeriodType().equals(BudgetPeriodType.MONTHLY)) {
            Integer period = budget.getPeriod();
            LocalDate newStartDate = budget.getEndDate().plusDays(1);
            LocalDate newEndDate = newStartDate.plusMonths(period).minusDays(1);

            budget.updateStartDate(newStartDate);
            budget.updateEndDate(newEndDate);
        } else if (budget.getPeriodType().equals(BudgetPeriodType.WEEKLY)) {
            Integer period = budget.getPeriod();
            LocalDate newStartDate = budget.getEndDate().plusDays(1);
            LocalDate newEndDate = newStartDate.plusWeeks(period).minusDays(1);

            budget.updateStartDate(newStartDate);
            budget.updateEndDate(newEndDate);
        } else {
            throw new CustomException(CommonResponseCode.SERVER_ERROR);
        }

        return budgetRepository.save(budget);
    }
}
