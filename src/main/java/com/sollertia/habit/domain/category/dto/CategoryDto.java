package com.sollertia.habit.domain.category.dto;

import com.sollertia.habit.domain.category.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable{
    private Long categoryId;
    private Category category;
}
