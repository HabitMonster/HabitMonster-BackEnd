package com.sollertia.habit.domain.habit.dto;

import lombok.*;

@Builder
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
    private int count;
    private String category;
    private Long sessionDuration;
}
