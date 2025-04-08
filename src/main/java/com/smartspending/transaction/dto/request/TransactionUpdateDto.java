package com.smartspending.transaction.dto.request;

import com.smartspending.transaction.enums.TransactionType;
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

    private BigDecimal amount;

    private LocalDate date;

    private String memo;

    private TransactionType type;

    private Long categoryId;
}
