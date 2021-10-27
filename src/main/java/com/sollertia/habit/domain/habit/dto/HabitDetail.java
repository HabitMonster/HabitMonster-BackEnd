package com.sollertia.habit.domain.habit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitDetail {
    private Long habitId;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private Long count;
    private String category;
    private Long sessionDuration;
}
