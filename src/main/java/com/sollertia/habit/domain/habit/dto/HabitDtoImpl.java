package com.sollertia.habit.domain.habit.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
@Builder
public class HabitDtoImpl {
    private Long habitId;
    @Size(min = 1, max = 120)
    private String title;
    @Size(min = 1, max = 120, message = "description size should under 140 characters")
    private String description;
    private String durationStart;
    private String durationEnd;
    private Long categoryId;
    private int count;
    private int totalCount;
    private String practiceDays;
}
