package com.smartspending.budget.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartspending.budget.enums.BudgetPeriodType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequestDto {

    private BigDecimal amount;
    private BudgetPeriodType periodType;
    private Integer period;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("isRecurring")
    private boolean isRecurring;

}
