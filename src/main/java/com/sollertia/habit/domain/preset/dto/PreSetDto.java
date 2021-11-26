package com.sollertia.habit.domain.preset.dto;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.preset.entity.PreSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}

