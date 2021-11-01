package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;
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
    private Long category;
    private int count;
    private int goalTime = 0;
    private String practiseDays;
}
