package com.sollertia.habit.domain.userteam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;

import javax.persistence.*;

@Entity
public class UserTeam {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;
}
