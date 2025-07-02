package com.smartspending.transaction.dto.response;

import com.smartspending.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String memo;
    private String type;
    private String category;
    private Long categoryId;

    public TransactionResponseDto(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.memo = transaction.getMemo();
        this.type = transaction.getType().name();
        this.category = transaction.getCategory().getName();
        this.categoryId = transaction.getCategory().getId();
    }
}
