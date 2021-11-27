package com.sollertia.habit.domain.habit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitUpdateRequestDto {
    private String title;
    @Size(min = 1, max = 120, message = "description size should under 140 characters")
    private String description;
    private int count;
}
