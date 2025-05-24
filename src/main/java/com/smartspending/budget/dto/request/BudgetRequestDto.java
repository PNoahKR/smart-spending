package com.smartspending.budget.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartspending.budget.enums.BudgetPeriodType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequestDto {

    @NotNull(message = "예산을 입력해주세요")
    @Positive(message = "양수만 입력 가능합니다")
    private BigDecimal amount;

    @NotNull
    private BudgetPeriodType periodType;

    @NotNull(message = "기간을 입력해주세요")
    private Integer period;

    @NotNull
    @JsonProperty("isActive")
    private boolean isActive;

    @NotNull
    @JsonProperty("isRecurring")
    private boolean isRecurring;

    @NotNull
    @JsonProperty("mainBudget")
    private boolean mainBudget;

}
