package com.sollertia.habit.domain.category.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CategoryResponseDto extends DefaultResponseDto implements Serializable{
        private List<CategoryDto> categories;
}
