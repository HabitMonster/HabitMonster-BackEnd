package com.sollertia.habit.domain.team;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.HabitWithTimer;
import com.sollertia.habit.domain.userteam.UserTeam;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

//    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
//    private List<Habit> habitsWithCounter;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<UserTeam> userTeam;


}
