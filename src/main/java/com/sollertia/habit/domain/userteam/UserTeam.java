package com.sollertia.habit.domain.userteam;

import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserTeam {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Team team;
}
