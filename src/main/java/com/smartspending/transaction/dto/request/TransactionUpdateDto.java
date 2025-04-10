package com.smartspending.transaction.dto.request;

import com.smartspending.transaction.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
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
public class TransactionUpdateDto {

    private Long id;

    @DecimalMin(value = "0.0", inclusive = true, message = "금액은 0 또는 양수여야 합니다")
    private BigDecimal amount;

    private LocalDate date;

    private String memo;

    private TransactionType type;

    private Long categoryId;
}
