package com.sollertia.habit.domain.completedhabbit.statics.service;

import com.sollertia.habit.domain.completedhabbit.statics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.user.entity.User;

public interface StatisticsService {

     StatisticsResponseDto getStatistics(User user, String date);

}
