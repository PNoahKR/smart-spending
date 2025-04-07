package com.smartspending.transaction.service;

import com.smartspending.category.entity.Category;
import com.smartspending.category.repository.CategoryRepository;
import com.smartspending.transaction.dto.request.TransactionRequestDto;
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
    public TransactionResponseDto create(TransactionRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow();
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
}
