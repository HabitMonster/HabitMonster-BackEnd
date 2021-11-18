package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class HabitDetail {
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
    private int achieveCount;
    private int totalCount;

    public static List<HabitDetail> listOf(List<Habit> habits) {
        List<HabitDetail> habitDetails = new ArrayList<>();
        for (Habit habit : habits) {
            habitDetails.add(of((HabitWithCounter) habit));
        }
        return habitDetails;
    }

    public static HabitDetail of(HabitWithCounter habit) {
        return HabitDetail.builder()
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
                .achieveCount(Math.toIntExact(habit.getAccomplishCounter() * habit.getGoalCountInSession() +
                        (habit.getIsAccomplishInSession() ? 0 : habit.getCurrent())))
                .totalCount(Math.toIntExact(habit.getWholeDays() * habit.getGoalCountInSession()))
                .build();
    }
}
