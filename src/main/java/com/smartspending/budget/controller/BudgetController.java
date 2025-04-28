package com.smartspending.budget.controller;

import com.smartspending.budget.dto.request.BudgetRequestDto;
import com.smartspending.budget.dto.response.BudgetResponseDto;
import com.smartspending.budget.service.BudgetService;
import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping("/create")
    public CommonResponse<BudgetResponseDto> create(@RequestBody BudgetRequestDto budgetRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponseUtil.success(budgetService.create(budgetRequestDto, userDetails.getUserId()));
    }
}
