package com.smartspending.dashboard.service;

import com.smartspending.dashboard.dto.response.DashboardResponseDto;

import java.time.LocalDate;

public interface DashboardService {

    DashboardResponseDto getDashboard(Long userId, LocalDate startDate, LocalDate endDate);
}
