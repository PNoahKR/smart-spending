package com.smartspending.transaction.dto.request;

import com.smartspending.transaction.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    private String memo;

    @NotNull
    private TransactionType type;

    @NotNull
    private Long categoryId;
}
