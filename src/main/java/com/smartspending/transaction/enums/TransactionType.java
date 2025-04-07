package com.smartspending.transaction.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    INCOME("수입"), EXPENSE("지출");

    private final String name;
}
