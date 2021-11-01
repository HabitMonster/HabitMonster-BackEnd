package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.Habit;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class HabitSummaryResponseVo {
    private Long habitId;
    private String title;
    private String durationStart;
    private String durationEnd;
    private Long current;
    private Long sessionDuration;
    private Long progressPercentage;
    private Category category;


    public HabitSummaryResponseVo(Habit habit) {
        this.habitId = habit.getId();
        this.title = habit.getTitle();
        this.durationStart = habit.getDurationStart().toString();
        this.durationEnd = habit.getDurationEnd().toString();
        this.current = habit.getCurrent();
        this.sessionDuration = habit.getSessionDuration();
        this.progressPercentage = habit.getAchievePercentage();
        this.category = habit.getCategory();

    }
}
