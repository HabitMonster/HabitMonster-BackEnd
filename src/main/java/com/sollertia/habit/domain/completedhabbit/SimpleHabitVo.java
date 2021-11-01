package com.sollertia.habit.domain.completedhabbit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleHabitVo {
    private String title;
    private int achievement;

    public SimpleHabitVo(CompletedHabit completedHabit) {
        this.title = completedHabit.getTitle();
        this.achievement = completedHabit.getAchievement();
    }
}
