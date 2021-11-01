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

    private User user = null;
    private Team team = null;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private String category;
    private Long count;
    private Long goalTime = 0l;
    private String practiseDays;
    private Category categoryType;
}
