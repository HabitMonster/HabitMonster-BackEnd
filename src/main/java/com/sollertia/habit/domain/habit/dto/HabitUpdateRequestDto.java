package com.sollertia.habit.domain.habit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitUpdateRequestDto {
    private String title;
    private String description;
    private int count;
}
