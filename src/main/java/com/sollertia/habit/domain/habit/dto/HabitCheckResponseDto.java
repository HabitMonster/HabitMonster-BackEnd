package com.sollertia.habit.domain.habit.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HabitCheckResponseDto extends DefaultResponseDto {
    private int current;
    private Boolean isAccomplished;
}
