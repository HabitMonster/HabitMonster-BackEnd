package com.sollertia.habit.domain.category.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class CategoryResponseDto extends DefaultResponseDto {
        private List<CategoryVo> categories;
}
