package com.sollertia.habit.domain.habit;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounter;
import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimer;

public interface  Habit {
    public static Habit getHabit(HabitDtoImpl habitDtoImpl) {
        Habit habit = null;
        switch (habitDtoImpl.getHabitType()) {
            case HABITWITHTIMER:
                habit = HabitWithTimer.createHabit(habitDtoImpl);
                break;
            case HABITWITHCOUNTER:
                habit = HabitWithCounter.createHabit(habitDtoImpl);
                break;
        }

        return habit;

    }

//    public Long getGoalPercentage();
}
