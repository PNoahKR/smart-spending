package com.smartspending.transaction.controller;

import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.response.PageInfoResponseDto;
import com.smartspending.common.util.ApiResponseUtil;
import com.smartspending.transaction.dto.request.TransactionRequestDto;
import com.smartspending.transaction.dto.request.TransactionUpdateDto;
import com.smartspending.transaction.dto.response.TransactionResponseDto;
import com.smartspending.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/list")
    public CommonResponse<PageInfoResponseDto<TransactionResponseDto>> getList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return ApiResponseUtil.success(
                transactionService.getTransactions(userDetails.getUserId(), from, to, pageable)
        );
    }

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
