package com.sollertia.habit.domain.category.dto;

import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class CategoryResponseDto extends DefaultResponseDto {
        private List<CategoryVo> categories;
}
