package com.sollertia.habit.domain.preset.dto;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.preset.entity.PreSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreSetDto {
    private Long presetId;
    private Long categoryId;
    private String title;
    private String description;
    private int period;
    private int count;
    private Category category;
    private Long userId;
    private String practiceDays;

    public PreSetDto(PreSet preSet) {
        this.presetId = preSet.getId();
        this.categoryId = preSet.getCategoryId();
        this.title = preSet.getTitle();
        this.description = preSet.getDescription();
        this.period = preSet.getPeriod();
        this.count = preSet.getCount();
        this.category = preSet.getCategory();
        this.practiceDays = preSet.getPracticeDays();
    }

    public PreSetDto(CompletedHabit completedHabit) {
        this.categoryId = completedHabit.getCategory().getCategoryId();
        this.title = completedHabit.getTitle();
        this.description = completedHabit.getDescription();
        this.count = Math.toIntExact(completedHabit.getGoalCount());
        this.category = completedHabit.getCategory();
        this.practiceDays = completedHabit.getPracticeDays();
        this.period = (int) Duration.between(completedHabit.getStartDate().atStartOfDay(),completedHabit.getEndupDate().atStartOfDay()).toDays();
        this.userId = completedHabit.getUser().getId();
    }
}
