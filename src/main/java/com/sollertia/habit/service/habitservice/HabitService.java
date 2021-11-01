package com.sollertia.habit.service.habitservice;

import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.utils.DefaultResponseDto;

import java.util.List;


public interface HabitService {

    public DefaultResponseDto createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto);

    public HabitDetailResponseDto getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) throws Throwable;

    public DefaultResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId);

    public DefaultResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId);

    public List<HabitSummaryResponseVo> getHabitSummaryList(Long userId) throws Throwable;

}
