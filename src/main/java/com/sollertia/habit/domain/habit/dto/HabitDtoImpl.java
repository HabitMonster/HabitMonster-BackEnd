package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.enums.Day;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;
import lombok.*;

import java.util.List;

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
    private Long count;
    private Long goalTime = 0l;
    private String practiseDays;
    private Category categoryType;
}
