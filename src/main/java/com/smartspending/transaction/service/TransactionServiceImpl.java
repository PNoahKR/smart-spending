package com.smartspending.transaction.service;

import com.smartspending.category.entity.Category;
import com.smartspending.category.repository.CategoryRepository;
import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import com.smartspending.transaction.dto.request.TransactionRequestDto;
import com.smartspending.transaction.dto.request.TransactionUpdateDto;
import com.smartspending.transaction.dto.response.TransactionResponseDto;
import com.smartspending.transaction.entity.Transaction;
import com.smartspending.transaction.repository.TransactionRepository;
import com.smartspending.user.entity.User;
import com.smartspending.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public TransactionResponseDto create(TransactionRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(CommonResponseCode.USER_NOT_FOUND));
        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new CustomException(CommonResponseCode.CATEGORY_NOT_FOUND));
        Transaction transaction = Transaction.builder()
                .user(user)
                .amount(requestDto.getAmount())
                .date(requestDto.getDate())
                .memo(requestDto.getMemo())
                .type(requestDto.getType())
                .category(category)
                .build();
        Transaction save = transactionRepository.save(transaction);
        return new TransactionResponseDto(save);
    }

    @Override
    @Transactional
    public TransactionResponseDto update(TransactionUpdateDto requestDto, Long userId) {
        Transaction transaction = transactionRepository.findById(requestDto.getId()).orElseThrow(() -> new CustomException(CommonResponseCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }

        if (requestDto.getAmount() != null) {
            transaction.updateAmount(requestDto.getAmount());
        }
        if (requestDto.getDate() != null) {
            transaction.updateDate(requestDto.getDate());
        }
        if (requestDto.getMemo() != null) {
            transaction.updateMemo(requestDto.getMemo());
        }
        if (requestDto.getType() != null) {
            transaction.updateType(requestDto.getType());
        }
        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new CustomException(CommonResponseCode.CATEGORY_NOT_FOUND));
            transaction.updateCategory(category);
        }
        return new TransactionResponseDto(transaction);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new CustomException(CommonResponseCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }

        transactionRepository.delete(transaction);
    }
}
