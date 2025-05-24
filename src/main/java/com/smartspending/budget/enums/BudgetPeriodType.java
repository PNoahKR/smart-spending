package com.smartspending.budget.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BudgetPeriodType {

    WEEKLY("주별"), MONTHLY("월별");

    private final String type;
}
