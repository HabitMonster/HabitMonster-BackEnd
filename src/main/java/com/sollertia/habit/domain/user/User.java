package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.domain.exp.Exp;
import com.sollertia.habit.domain.habbit.Habbit;
import com.sollertia.habit.domain.userteam.UserTeam;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String password;

    private ProviderType providerType;

    @OneToMany(mappedBy = "user")
    private List<Habbit> habits;

    @OneToMany(mappedBy = "user")
    private List<UserTeam> userTeam;

    @ManyToOne
    private Avatar avatar;

    @OneToOne
    private Exp exp;
}
