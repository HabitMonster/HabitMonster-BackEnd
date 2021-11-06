package com.sollertia.habit.domain.preset.dto;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.preset.entity.PreSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
@AllArgsConstructor
@Builder
public class PreSetVo {
    private Long presetId;
    private Long categoryId;
    private String title;
    private String description;
    private int period;
    private int count;
    private Category category;
    private Long userId;
    private String practiceDays;

    public PreSetVo(PreSet preSet) {
        this.presetId = preSet.getId();
        this.categoryId = preSet.getCategoryId();
        this.title = preSet.getTitle();
        this.description = preSet.getDescription();
        this.period = preSet.getPeriod();
        this.count = preSet.getCount();
        this.category = preSet.getCategory();
        this.practiceDays = preSet.getPracticeDays();
    }

    public PreSetVo(Habit habit) {
        this.categoryId = habit.getCategory().getCategoryId();
        this.title = habit.getTitle();
        this.description = habit.getDescription();
        this.count = habit.getGoal();
        this.category = habit.getCategory();
        this.practiceDays = habit.getPracticeDays();
        this.period = (int) Duration.between(habit.getDurationStart().atStartOfDay(),habit.getDurationEnd().atStartOfDay()).toDays();
        this.userId = habit.getUser().getId();
    }
}
