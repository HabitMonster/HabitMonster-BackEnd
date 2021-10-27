package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.enums.HabitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateHabitRequestDto {
    private HabitType habitType = HabitType.HABITWITHCOUNTER;
    private Long goalTime = 0l;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private Long count;
    private String category;
    private Long sessionDuration;
}
