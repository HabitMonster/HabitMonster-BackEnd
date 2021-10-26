package com.sollertia.habit.domain.team;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.userteam.UserTeam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Habit> habits;

    @OneToMany(mappedBy = "team")
    private List<UserTeam> userTeam;


}
