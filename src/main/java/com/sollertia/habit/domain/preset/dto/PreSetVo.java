package com.sollertia.habit.domain.preset.dto;

import com.sollertia.habit.domain.category.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PreSetVo {
    private Long presetId;
    private Long categoryId;
    private String title;
    private String description;
    private int period;
    private Long count;
    private Category category;
    private String practiseDays;
}
