package com.sollertia.habit.domain.habit;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounter;
import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimer;


public class HabitFactory {
    public static Habit createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl habitDto) {

        Habit habit = null;

        switch (habitTypeDto.getHabitType()) {

            case HABITWITHTIMER:
                habit = HabitWithTimer.createHabit(habitTypeDto.getHabitSession(), habitDto);
                break;
            case HABITWITHCOUNTER:
                habit = HabitWithCounter.createHabit(habitTypeDto.getHabitSession(), habitDto);
                break;
        }

        return habit;
    }
}
