package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.Habit;
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
    private Long progressPercentage;
    private Category category;

    public static List<HabitSummaryVo> listOf(List<Habit> habits) {
        List<HabitSummaryVo> summaryVoList = new ArrayList<>();
        for (Habit habit : habits) {
            summaryVoList.add(of(habit));
        }
        return summaryVoList;
    }

    public static HabitSummaryVo of(Habit habit) {
        return HabitSummaryVo.builder()
                .habitId(habit.getId())
                .title(habit.getTitle())
//                .durationStart(habit.getDurationStart())
//                .durationEnd(habit.getDurationEnd())
                .count(habit.getCount())
                .current(habit.getCurrent())
                .sessionDuration(habit.getSessionDuration())
                .progressPercentage(habit.getAchievePercentage())
                .category(habit.getCategory())
                .build();
    }
}
