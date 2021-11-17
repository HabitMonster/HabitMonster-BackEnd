package com.sollertia.habit.domain.habit.dto;

import lombok.*;

@Builder
@Getter
public class HabitDetail {
    private Long habitId;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private int count;
    private int totalCount;
    private String category;
    private String practiceDays;
    private int current;
    private Long achievePercentage;
}
