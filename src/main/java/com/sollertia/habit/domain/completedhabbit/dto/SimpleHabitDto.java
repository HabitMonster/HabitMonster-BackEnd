package com.sollertia.habit.domain.completedhabbit.dto;

import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleHabitDto {
    private String title;
    private Long achievement;
    private Long accomplishCount;
    private Long goalCount;
    private boolean isSuccess;

    private String startDate;
    private String endUPDate;

    public SimpleHabitDto(CompletedHabit completedHabit) {
        this.title = completedHabit.getTitle();
        this.achievement = completedHabit.getAchievementPercentage();
        this.accomplishCount = completedHabit.getAccomplishCounter();
        this.goalCount = completedHabit.getGoalCount();
        this.isSuccess = completedHabit.getIsSuccess();
        this.startDate = completedHabit.getStartDate().toString();
        this.endUPDate = completedHabit.getEndupDate().toString();
    }
}
