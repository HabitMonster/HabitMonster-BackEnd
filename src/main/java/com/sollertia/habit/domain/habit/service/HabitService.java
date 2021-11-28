package com.sollertia.habit.domain.habit.service;


import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.DefaultResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface HabitService {

     DefaultResponseDto createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto, User user);

     HabitDetailResponseDto getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) throws Throwable;

     HabitCheckResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId, LocalDate today);

     DefaultResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId, User user);

     List<HabitSummaryVo> getHabitSummaryList(User user, LocalDate today) throws Throwable;

    HabitDetailResponseDto updateHabit(HabitTypeDto habitTypeDto, Long habitId, HabitUpdateRequestDto habitUpdateRequestDto, User user);
}
