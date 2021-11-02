package com.sollertia.habit.domain.completedhabbit.statisticsservice;

import com.sollertia.habit.domain.completedhabbit.StatisticsResponseDto;
import com.sollertia.habit.domain.user.User;

public interface StatisticsService {

    public StatisticsResponseDto getStatistics(User user, String date);

}
