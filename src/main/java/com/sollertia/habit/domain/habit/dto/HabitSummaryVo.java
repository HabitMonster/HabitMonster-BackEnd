package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class HabitSummaryVo {
    private Long habitId;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private int count;
    private int current;
    private Boolean isAccomplished;
    private String practiceDays;
    private Long achievePercentage;
    private Category category;
    private Long categoryId;
    private int achieveCount;
    private int totalCount;

    public static List<HabitSummaryVo> listOf(List<Habit> habits) {
        List<HabitSummaryVo> summaryVoList = new ArrayList<>();
        for (Habit habit : habits) {
            summaryVoList.add(of((HabitWithCounter) habit));
        }
        return summaryVoList;
    }

    public static HabitSummaryVo of(HabitWithCounter habit) {
        return HabitSummaryVo.builder()
                .habitId(habit.getId())
                .title(habit.getTitle())
                .description(habit.getDescription())
                .durationStart(habit.getDurationStart().toString())
                .durationEnd(habit.getDurationEnd().toString())
                .count(habit.getGoalCountInSession())
                .isAccomplished(habit.getIsAccomplishInSession())
                .practiceDays(habit.getPracticeDays())
                .current(habit.getCurrent())
                .achievePercentage(habit.getAchievePercentage())
                .category(habit.getCategory())
                .categoryId(habit.getCategory().getCategoryId())
                .achieveCount(Math.toIntExact(habit.getAccomplishCounter() * habit.getGoalCountInSession() +
                        (habit.getIsAccomplishInSession() ? 0 : habit.getCurrent())))
                .totalCount(Math.toIntExact(habit.getWholeDays() * habit.getGoalCountInSession()))
                .build();
    }
}
