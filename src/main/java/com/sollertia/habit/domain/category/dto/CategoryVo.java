package com.sollertia.habit.domain.category.dto;

import com.sollertia.habit.domain.category.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CategoryVo {
    private Long categoryId;
    private Category category;
}
