package com.sollertia.habit.service.habitservice;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitDetail;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.dto.ResponseDto;


public interface HabitService {

    public Habit createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto);

    public HabitDetail getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) throws Throwable;

    public ResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId);

    public ResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId);

}
