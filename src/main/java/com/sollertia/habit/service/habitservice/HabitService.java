package com.sollertia.habit.service.habitservice;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.dto.*;

import java.util.List;


public interface HabitService {

    public Habit createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto);

    public HabitDetail getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) throws Throwable;

    public ResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId);

    public ResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId);

    public List<HabitSummaryResponseDto> getHabitSummaryList(Long userId);

}
