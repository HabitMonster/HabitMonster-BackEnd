package com.sollertia.habit.domain.team;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.userteam.UserTeam;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Habit> habits;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<UserTeam> userTeam;


}
