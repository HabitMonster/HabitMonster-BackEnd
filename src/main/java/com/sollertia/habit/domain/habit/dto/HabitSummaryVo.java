package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.HabitWithCounter;
import com.sollertia.habit.domain.habit.enums.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class HabitSummaryVo {
    private Long habitId;
    private String title;
    private String durationStart;
    private String durationEnd;
    private Long count;
    private Long current;
    private Long sessionDuration;
    private Long achievePercentage;
    private Category category;

    public static List<HabitSummaryVo> listOf(List<HabitWithCounter> habits) {
        List<HabitSummaryVo> summaryVoList = new ArrayList<>();
        for (HabitWithCounter habit : habits) {
            summaryVoList.add(of(habit));
        }
        return summaryVoList;
    }

    public static HabitSummaryVo of(HabitWithCounter habit) {
        return HabitSummaryVo.builder()
                .habitId(habit.getId())
                .title(habit.getTitle())
                .durationStart(habit.getDurationStart().toString())
                .durationEnd(habit.getDurationEnd().toString())
                .count(habit.getGoalCountInSession())
                .current(habit.getCurrent())
                .sessionDuration(habit.getSessionDuration())
                .achievePercentage(habit.getAchievePercentage())
                .category(habit.getCategory())
                .build();
    }
}
