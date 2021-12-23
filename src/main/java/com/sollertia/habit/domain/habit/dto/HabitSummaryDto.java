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
public class HabitSummaryDto {
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

    public static List<HabitSummaryDto> listOf(List<Habit> habits) {
        List<HabitSummaryDto> summaryVoList = new ArrayList<>();
        for (Habit habit : habits) {
            summaryVoList.add(of((HabitWithCounter) habit));
        }
        return summaryVoList;
    }

    public static HabitSummaryDto of(Habit habit) {
        return HabitSummaryDto.builder()
                .habitId(habit.getId())
                .title(habit.getTitle())
                .description(habit.getDescription())
                .durationStart(habit.getDurationStart().toString())
                .durationEnd(habit.getDurationEnd().toString())
                .count(habit.getGoalInPeriod())
                .isAccomplished(habit.getIsAccomplishInPeriod())
                .practiceDays(habit.getPracticeDays())
                .current(habit.getCurrent())
                .achievePercentage(habit.getAchievePercentage())
                .category(habit.getCategory())
                .categoryId(habit.getCategory().getCategoryId())
                .achieveCount(Math.toIntExact(habit.getAccomplishCounter() * habit.getGoalInPeriod() +
                        (habit.getIsAccomplishInPeriod() ? 0 : habit.getCurrent())))
                .totalCount(Math.toIntExact(habit.getWholeDays() * habit.getGoalInPeriod()))
                .build();
    }
}
