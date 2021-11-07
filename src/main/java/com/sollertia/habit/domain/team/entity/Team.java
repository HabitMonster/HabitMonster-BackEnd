package com.sollertia.habit.domain.team.entity;

import com.sollertia.habit.domain.userteam.entity.UserTeam;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
//    private List<Habit> habitsWithCounter;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<UserTeam> userTeam;


}
