package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.category.enums.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitDtoImpl {


    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private Long categoryId;
    private int count;
    private String practiceDays;
}
