package com.smartspending.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeExpenseChartDto {
    private LocalDate date;
    private BigDecimal income;
    private BigDecimal expense;
}
