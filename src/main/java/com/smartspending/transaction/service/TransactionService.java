package com.smartspending.transaction.service;

import com.smartspending.transaction.dto.request.TransactionRequestDto;
import com.smartspending.transaction.dto.request.TransactionUpdateDto;
import com.smartspending.transaction.dto.response.TransactionResponseDto;

public interface TransactionService {

    TransactionResponseDto create(TransactionRequestDto requestDto, Long userId);

    TransactionResponseDto update(TransactionUpdateDto requestDto, Long userId);

    void delete(Long id, Long userId);
}
