package com.smartspending.transaction.controller;

import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import com.smartspending.transaction.dto.request.TransactionRequestDto;
import com.smartspending.transaction.dto.request.TransactionUpdateDto;
import com.smartspending.transaction.dto.response.TransactionResponseDto;
import com.smartspending.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    public CommonResponse<TransactionResponseDto> create(@RequestBody @Valid TransactionRequestDto transactionRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponseUtil.success(transactionService.create(transactionRequestDto, userDetails.getUserId()));
    }

    @PatchMapping("/update/{id}")
    public CommonResponse<TransactionResponseDto> update(@PathVariable Long id,
                                                         @RequestBody @Valid TransactionUpdateDto transactionUpdateDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        transactionUpdateDto.setId(id);
        return ApiResponseUtil.success(transactionService.update(transactionUpdateDto, userDetails.getUserId()));
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        transactionService.delete(id, userDetails.getUserId());
        return ApiResponseUtil.success();
    }
}
