package com.smartspending.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDto {
    private List<IncomeExpenseChartDto> incomeExpenseByDate;
    private List<CategoryStatisticDto> spendingByCategory;
    private SummaryDto summary;
    private List<TransactionSummaryDto> recentTransactions;
}
