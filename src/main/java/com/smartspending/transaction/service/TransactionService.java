package com.smartspending.transaction.service;

import com.smartspending.common.response.PageInfoResponseDto;
import com.smartspending.transaction.dto.request.TransactionRequestDto;
import com.smartspending.transaction.dto.request.TransactionUpdateDto;
import com.smartspending.transaction.dto.response.TransactionResponseDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TransactionService {

    PageInfoResponseDto<TransactionResponseDto> getTransactions(
            Long userId, LocalDate from, LocalDate to, Pageable pageable
    );

    TransactionResponseDto create(TransactionRequestDto requestDto, Long userId);

    TransactionResponseDto update(TransactionUpdateDto requestDto, Long userId);

    void delete(Long id, Long userId);
}
