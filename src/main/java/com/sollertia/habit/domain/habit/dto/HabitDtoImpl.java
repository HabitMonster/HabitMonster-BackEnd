package com.sollertia.habit.domain.habit.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitDtoImpl {
    private Long habitId;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private Long categoryId;
    private int count;
    private int totalCount;
    private String practiceDays;
}
