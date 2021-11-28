package com.sollertia.habit.domain.statistics.service;

import com.sollertia.habit.domain.statistics.dto.GlobalStatisticsResponseDto;
import com.sollertia.habit.domain.statistics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.user.entity.User;

public interface StatisticsService {

     StatisticsResponseDto getStatistics(User user, String date);

    GlobalStatisticsResponseDto getGlobalStatistics();
}
