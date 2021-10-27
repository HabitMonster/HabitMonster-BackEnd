package com.sollertia.habit.domain.habit.service;

import com.sollertia.habit.domain.habit.dto.CreateHabitRequestDto;
import com.sollertia.habit.domain.habit.dto.HabitDetail;
import com.sollertia.habit.domain.habit.dto.ResponseDto;

public interface HabitService {

    public ResponseDto createHabit(CreateHabitRequestDto createHabitRequestDto);

    public HabitDetail getHabitDetail(Long habitId);

    public ResponseDto checkHabit(Long habitId);

    public ResponseDto deleteHabit(Long habitId);

}
