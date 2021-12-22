package com.sollertia.habit.domain.category.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto implements Serializable{
        private List<CategoryDto> categories;
        private Integer statusCode;
        private String responseMessage;
}
