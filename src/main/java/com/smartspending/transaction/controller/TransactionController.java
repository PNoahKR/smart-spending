package com.smartspending.transaction.controller;

import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import com.smartspending.transaction.dto.request.TransactionRequestDto;
import com.smartspending.transaction.dto.response.TransactionResponseDto;
import com.smartspending.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    public CommonResponse<TransactionResponseDto> create(@RequestBody TransactionRequestDto transactionRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponseUtil.success(transactionService.create(transactionRequestDto, userDetails.getUserId()));
    }
}
