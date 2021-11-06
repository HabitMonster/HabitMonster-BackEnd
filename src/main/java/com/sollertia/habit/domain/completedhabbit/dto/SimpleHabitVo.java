package com.sollertia.habit.domain.completedhabbit.dto;

import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleHabitVo {
    private String title;
    private Long achievement;

    public SimpleHabitVo(CompletedHabit completedHabit) {
        this.title = completedHabit.getTitle();
        this.achievement = completedHabit.getAchievementPercentage();
    }
}
