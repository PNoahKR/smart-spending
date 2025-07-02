package com.smartspending.budget.controller;

import com.smartspending.budget.dto.request.BudgetRequestDto;
import com.smartspending.budget.dto.response.BudgetResponseDto;
import com.smartspending.budget.service.BudgetService;
import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping("/list")
    public CommonResponse<List<BudgetResponseDto>> getBudgets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponseUtil.success(budgetService.getBudgets(userDetails.getUserId()));
    }


    @PostMapping("/create")
    public CommonResponse<BudgetResponseDto> create(@RequestBody BudgetRequestDto budgetRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponseUtil.success(budgetService.create(budgetRequestDto, userDetails.getUserId()));
    }

    @PatchMapping("/update/{id}")
    public CommonResponse<BudgetResponseDto> update(@PathVariable Long id,
                                                    @RequestBody BudgetRequestDto budgetRequestDto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponseUtil.success(budgetService.update(id, budgetRequestDto, userDetails.getUserId()));
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        budgetService.delete(id, userDetails.getUserId());
        return ApiResponseUtil.success();
    }
}
