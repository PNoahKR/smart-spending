package com.smartspending.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryDto {
    private LocalDate date;
    private String type;        // "INCOME" 또는 "EXPENSE"
    private String category;    // 카테고리 이름
    private BigDecimal amount;
}
