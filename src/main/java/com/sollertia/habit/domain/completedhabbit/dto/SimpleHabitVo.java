package com.sollertia.habit.domain.completedhabbit.dto;

import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SimpleHabitVo {
    private String title;
    private Long achievement;
    private Long accomplishCount;
    private Long goalCount;

    private String startDate;
    private String endUPDate;

    public SimpleHabitVo(CompletedHabit completedHabit) {
        this.title = completedHabit.getTitle();
        this.achievement = completedHabit.getAchievementPercentage();
        this.accomplishCount = completedHabit.getAccomplishCounter();
        this.goalCount = completedHabit.getGoalCount();
        this.startDate = completedHabit.getStartDate().toString();
        this.endUPDate = completedHabit.getEndupDate().toString();
    }
}
