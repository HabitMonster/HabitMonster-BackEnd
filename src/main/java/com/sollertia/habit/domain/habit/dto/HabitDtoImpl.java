package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.enums.Day;
import com.sollertia.habit.domain.habit.enums.HabitSession;
import com.sollertia.habit.domain.habit.enums.HabitType;
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

    private HabitType habitType = HabitType.HABITWITHCOUNTER;
    private HabitSession habitSession = HabitSession.NPERDAY;
    private Long goalTime = 0l;
    private User user = null;
    private Team team = null;
    private String title;
    private String description;
    private String durationStart;
    private String durationEnd;
    private Long count;
    private String category;
    private Long sessionDuration;
    private List<Day> dayList;

}
