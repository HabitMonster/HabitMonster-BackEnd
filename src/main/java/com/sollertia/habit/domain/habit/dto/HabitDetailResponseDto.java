package com.sollertia.habit.domain.habit.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitDetailResponseDto extends DefaultResponseDto {
    private HabitDetail habitDetail;
}
