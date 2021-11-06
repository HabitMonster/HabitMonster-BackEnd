package com.sollertia.habit.domain.habit.service;


import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.DefaultResponseDto;

import java.util.List;

public interface HabitService {

     DefaultResponseDto createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto, User user);

     HabitDetailResponseDto getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) throws Throwable;

     HabitCheckResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId);

     DefaultResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId, User user);

     List<HabitSummaryVo> getHabitSummaryList(User user) throws Throwable;

}
