package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.enums.Day;
import com.sollertia.habit.domain.preset.enums.Category;
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

    private User user = null;
    private Team team = null;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private String category;
    private Long count;
    private Long sessionDuration;
    private List<Day> dayList = null;
    private Long goalTime = 0l;
    private List<Integer> week;
    private Category categoryType;
}
