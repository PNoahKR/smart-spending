package com.smartspending.transaction.dto.request;

import com.smartspending.transaction.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
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
    @DecimalMin(value = "0.0", inclusive = true, message = "금액은 0 또는 양수여야 합니다.")
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    private String memo;

    @NotNull
    private TransactionType type;

    @NotNull
    private Long categoryId;
}
