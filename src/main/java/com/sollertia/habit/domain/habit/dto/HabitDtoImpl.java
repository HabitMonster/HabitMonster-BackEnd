package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.enums.Day;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitDtoImpl {

    private User user = null;
    private Team team = null;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private String category;
    private String dayList;
    private Long count;
    private Long goalTime = 0l;

}
