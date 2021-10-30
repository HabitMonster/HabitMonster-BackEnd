package com.sollertia.habit.domain.preset.dto;

import com.sollertia.habit.domain.preset.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CategoryVo {
    private int categoryId;
    private Category category;
}
