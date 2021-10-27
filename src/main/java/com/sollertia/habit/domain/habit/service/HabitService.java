package com.sollertia.habit.domain.habit.service;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitDetail;
import com.sollertia.habit.domain.habit.dto.ResponseDto;

public interface HabitService {

    public ResponseDto createHabit(HabitDtoImpl createHabitRequestDto);

    public HabitDetail getHabitDetail(Long habitId);

    public ResponseDto checkHabit(Long habitId);

    public ResponseDto deleteHabit(Long habitId);

}
