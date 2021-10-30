package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class HabitSummaryResponseDto extends DefaultResponseDto {
    private Long habitId;
    private String title;
    private String durationStart;
    private String durationEnd;
    private Long count;
    private Long current;
    private Long sessionDuration;
    private Long progressPercentage;
    private Category category;
}
